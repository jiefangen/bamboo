package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.business.admin.v1.common.constant.SystemConstants;
import org.panda.business.admin.v1.common.constant.enums.RoleCode;
import org.panda.tech.data.mybatis.config.QueryPageHelper;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.MenuVO;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
import org.panda.business.admin.v1.modules.system.service.SysMenuService;
import org.panda.business.admin.v1.modules.system.service.SysUserService;
import org.panda.business.admin.v1.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.panda.business.admin.v1.modules.system.service.entity.SysUser;
import org.panda.business.admin.v1.modules.system.service.repository.SysRoleMapper;
import org.panda.business.admin.v1.modules.system.service.repository.SysUserMapper;
import org.panda.tech.core.web.jwt.InternalJwtResolver;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.security.user.UserSpecificDetails;
import org.panda.tech.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-20
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUserDto getUserAndRoles(String username) {
        SysUser userParam = new SysUser();
        userParam.setUsername(username);
        SysUserDto sysUserDto = this.baseMapper.findUserAndRoles(userParam);
        if (sysUserDto == null) {
            return null;
        }
        List<SysRole> roles = sysUserDto.getRoles();
        if(CollectionUtils.isNotEmpty(roles)) {
            Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
            sysUserDto.setRoleCodes(roleCodes);
        }
        return sysUserDto;
    }

    @Override
    public QueryResult<UserVO> getUserByPage(UserQueryParam queryParam) {
        Page<SysUser> page = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryWrapper.like(SysUser::getUsername, queryParam.getKeyword())
                        .like(SysUser::getNickname, queryParam.getKeyword());
        }
        queryWrapper.orderByAsc(SysUser::getCreateTime);
        IPage<SysUser> userPage = this.page(page, queryWrapper);

        List<UserVO> userVOList = new ArrayList<>();
        if (userPage.getTotal() > 0) {
            List<SysUser> users = userPage.getRecords();
            users.forEach(user -> {
                UserVO userVO = new UserVO();
                userVO.setUser(user);
                List<SysRole> roles = sysRoleMapper.findRolesByUserId(user.getId());
                if(CollectionUtils.isNotEmpty(roles)) {
                    Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
                    userVO.setRoleCodes(roleCodes);
                    userVO.setRoles(roles);
                }
                userVOList.add(userVO);
            });
        }
        QueryResult<UserVO> queryResult = QueryPageHelper.convertQueryResult(userVOList, userPage);
        return queryResult;
    }

    @Override
    public UserVO getUserByToken(String token) {
        UserSpecificDetails userSpecificDetails;
        if (SecurityUtil.isAuthorized()) {
            userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        } else {
            userSpecificDetails = this.jwtResolver.parse(token, UserSpecificDetails.class);
        }
        String username = userSpecificDetails.getUsername();
        SysUserDto sysUserDto = this.getUserAndRoles(username);

        UserVO userVO = new UserVO();
        userVO.setUser(sysUserDto.getUser());
        userVO.setRoleCodes(sysUserDto.getRoleCodes());

        List<MenuVO> routes = menuService.getRoutes();
        userVO.setRoutes(routes);
        return userVO;
    }

    @Override
    public SysUser getUserInfo(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean addUser(SysUser user) {
        // 校验username不能重复
        String username = user.getUsername();
        SysUser userPO = this.getUserInfo(username);
        if (userPO != null) {
            String msg = "The username is already taken!";
            LogUtil.error(getClass(), msg);
            return false;
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return this.save(user);
    }

    @Override
    public boolean updateUser(SysUser user) {
        return this.updateById(user);
    }

    @Override
    public boolean deleteUser(String username) throws BusinessException {
        if (!this.checkTopRoles()) {
            return false;
        }

        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        // 删除操作排除自己
        String principalUsername = userSpecificDetails.getUsername();
        if (username.equals(principalUsername)) {
            throw new BusinessException("Can't delete yourself.");
        }

        // 校验是否绑定的有角色
        SysUserDto useDto = this.getUserAndRoles(username);
        List<SysRole> roles = useDto.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            throw new BusinessException("Please unbind this user's role first.");
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.remove(queryWrapper);
    }

    @Override
    public void updateUserRole(SysUserDto userDto) {
        // 更新用户角色
        Integer userId = userDto.getUserId();
        this.baseMapper.updateUserRole(userId, userDto.getRoleCodes());
    }

    @Override
    public String resetPassword(String username, String oldPassword, String newPassword) {
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        String principalUsername = userSpecificDetails.getUsername();
        SysUser sysUser = null;
        // 具有相应角色权限的管理员才可以重置, 本人可以重置自己的密码,无需验证
        if (!username.equals(principalUsername)) {
            // 判断具有相应角色角色才可以更新
            if (this.checkTopRoles()) {
                sysUser = this.getUserInfo(username);
            } else {
                return SystemConstants.ROLE_NOT_CHANGE_PASS;
            }
        }

        // 判断旧密码是否正确
        if (!passwordEncoder.matches(sysUser.getPassword(), userSpecificDetails.getPassword())) {
            return SystemConstants.PWD_WRONG;
        }

        String newPasswordEncrypt = passwordEncoder.encode(newPassword);
        sysUser.setPassword(newPasswordEncrypt);
        this.updateUser(sysUser);
        return Commons.RESULT_SUCCESS;
    }

    public boolean checkTopRoles() {
        UserSpecificDetails userSpecificDetails = SecurityUtil.getAuthorizedUserDetails();
        // 删除操作排除自己
        String principalUsername = userSpecificDetails.getUsername();

        SysUserDto useDto = this.getUserAndRoles(principalUsername);
        List<SysRole> roles = useDto.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        List<String> topRoles = RoleCode.getTopRoles();
        List<SysRole> result = roles.stream()
                .filter(role -> topRoles.contains(role.getRoleCode()))
                .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(result);
    }

}
