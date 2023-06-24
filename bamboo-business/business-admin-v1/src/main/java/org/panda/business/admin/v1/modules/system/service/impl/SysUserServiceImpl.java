package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.business.admin.v1.common.util.QueryPageUtil;
import org.panda.business.admin.v1.modules.system.api.param.UserQueryParam;
import org.panda.business.admin.v1.modules.system.api.vo.UserVO;
import org.panda.business.admin.v1.modules.system.service.ISysUserRoleService;
import org.panda.business.admin.v1.modules.system.service.ISysUserService;
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
import org.springframework.stereotype.Service;

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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private InternalJwtResolver jwtResolver;
    @Autowired
    private ISysUserRoleService userRoleService;

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
        QueryResult<UserVO> queryResult = QueryPageUtil.convertQueryResult(userVOList, userPage);
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
        SysUserDto sysUserDto = userRoleService.getUserAndRoles(username);

        UserVO userVO = new UserVO();
        userVO.setUser(sysUserDto.getUser());
        userVO.setRoleCodes(sysUserDto.getRoleCodes());

//        List<MenuVO> routes = menuService.getRoutes();
//        userVO.setRoutes(routes);
        return userVO;
    }

//    @Override
//    public UserPO getUserInfo(String username) {
//        return null;
//    }
//
//    @Override
//    public UserDTO getUserAndRoles(String username) {
//        return null;
//    }
//
//    @Override
//    public String addUser(UserPO user) {
//        return null;
//    }
//
//    @Override
//    public int updateUser(UserPO user) {
//        return 0;
//    }
//
//    @Override
//    public int deleteUser(String username) throws SystemException {
//        return 0;
//    }
//
//    @Override
//    public boolean checkRoleUpdatedPass() {
//        return false;
//    }
//
//    @Override
//    public void updateUserRole(UserDTO userDTO) {
//
//    }
}
