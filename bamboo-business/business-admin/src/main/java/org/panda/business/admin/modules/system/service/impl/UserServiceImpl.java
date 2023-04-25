package org.panda.business.admin.modules.system.service.impl;

import com.github.pagehelper.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.panda.business.admin.common.constant.enums.RoleType;
import org.panda.business.admin.common.model.ResultConstant;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.common.utils.DateUtil;
import org.panda.business.admin.modules.system.dao.RoleDao;
import org.panda.business.admin.modules.system.dao.UserDao;
import org.panda.business.admin.modules.system.model.dto.UserDTO;
import org.panda.business.admin.modules.system.model.po.RolePO;
import org.panda.business.admin.modules.system.model.po.UserPO;
import org.panda.business.admin.modules.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Page<UserDTO> getUsers(String keyword) {
        Page<UserDTO> users = userDao.findUsers(keyword);
        users.forEach(user -> {
            user.setCreateTimeStr(DateUtil.format(user.getCreateTime(), DateUtil.LONG_DATE_PATTERN));
            user.setRoles(roleDao.findByUserId(user.getId()));
            List<RolePO> roles = roleDao.findByUserId(user.getId());
            if(CollectionUtils.isNotEmpty(roles)) {
                Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
                user.setRoleCodes(roleCodes);
            }
        });
        return users;
    }

    @Override
    public UserPO getUserInfo(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public UserDTO getUserAndRoles(String username) {
        UserPO userPO = userDao.findByUsername(username);
        UserDTO userDto = new UserDTO(userPO);
        List<RolePO> roles = roleDao.findByUserId(userPO.getId());
        userDto.setRoles(roles);
        if(CollectionUtils.isNotEmpty(roles)) {
            Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
            userDto.setRoleCodes(roleCodes);
        }
        return userDto;
    }

    @Override
    public String addUser(UserPO user) {
        // 校验username不能重复
        String username = user.getUsername();
        UserPO userPO = userDao.findByUsername(username);
        if (userPO != null) {
            String msg = "The username is already taken!";
            LOGGER.error(msg);
            return msg;
        }
        userDao.insertUser(user);
        return ResultConstant.DEFAULT_SUCCESS_MSG;
    }

    @Override
    public int updateUser(UserPO user) {
        // 更新时间
        user.setUpdateTime(new Date());
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(String username) throws SystemException {
        if (!this.checkTopRoles()) {
            return -1;
        }

        // 删除操作排除自己
        UserPO user = (UserPO) SecurityUtils.getSubject().getPrincipal();
        String principalUsername = user.getUsername();
        if (username.equals(principalUsername)) {
            throw new SystemException("Can't delete yourself.");
        }

        // 校验是否绑定的有角色
        UserDTO useDto = this.getUserAndRoles(username);
        List<RolePO> roles = useDto.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            throw new SystemException("Please unbind this user's role first.");
        }

        return userDao.deleteUser(username);
    }

    @Override
    public boolean checkRoleUpdatedPass() {
        return this.checkTopRoles();
    }

    @Override
    public void updateUserRole(UserDTO userDTO) {
        // 更新用户角色
        BigInteger userId = userDTO.getId();
        userDao.updateUserRole(userId, userDTO.getRoleCodes());
    }

    public boolean checkTopRoles() {
        UserPO user = (UserPO) SecurityUtils.getSubject().getPrincipal();
        String principalUsername = user.getUsername();

        UserDTO useDto = this.getUserAndRoles(principalUsername);
        List<RolePO> roles = useDto.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        List<String> topRoles = RoleType.getTopRoles();
        List<RolePO> result = roles.stream()
                .filter(role -> topRoles.contains(role.getRoleCode()))
                .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(result);
    }
}
