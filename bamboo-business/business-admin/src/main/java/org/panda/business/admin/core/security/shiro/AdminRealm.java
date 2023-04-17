package org.panda.business.admin.core.security.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.panda.business.admin.common.constant.SystemConstants;
import org.panda.business.admin.common.utils.ApplicationContextUtil;
import org.panda.business.admin.modules.system.dao.UserDao;
import org.panda.business.admin.modules.system.domain.po.RolePO;
import org.panda.business.admin.modules.system.domain.po.UserPO;
import org.panda.business.admin.modules.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiefangen
 * @since JDK 1.8  2020/5/11
 **/
public class AdminRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizingRealm.class);

    @Autowired
    private UserDao userDao;

    /**
     * 用户登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null) {
//            LOGGER.error(SystemConstant.USER_EMPTY);
            throw new AuthenticationException(SystemConstants.USER_EMPTY);
        }
        String username = authenticationToken.getPrincipal().toString();

        // 通过用户名在数据库查到该用户的信息
        UserPO user = userDao.findByUsername(username);
        if (user == null) {
            LOGGER.warn(SystemConstants.USERNAME_NOT_EXIST);
            throw new UnknownAccountException(SystemConstants.USERNAME_NOT_EXIST);
        } else {
            if (user.getDisabled() != 0) {
                throw new AccountException(SystemConstants.USER_DISABLED);
            }

            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()), getName());
            return simpleAuthenticationInfo;
        }
    }

    /**
     * 系统接口鉴权认证
     * 该用户角色拥有相应的接口资源才可以访问
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserPO user = (UserPO) principalCollection.getPrimaryPrincipal();
        UserService userService = ApplicationContextUtil.getBean(UserService.class);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<RolePO> roles = userService.getUserAndRoles(user.getUsername()).getRoles();
        //添加角色鉴权,对应使用注解@RequiresRoles()
        Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleCodes);

        // TODO 添加权限鉴权
        // 每次访问带有权限限制的接口时就会验证，拥有对应权限code的话就可以正常访问。
//        simpleAuthorizationInfo.addStringPermissions(systemMenuList.stream()
//                .map(systemMenu -> systemMenu.getPermission())
//                .collect(Collectors.toList()));
        return simpleAuthorizationInfo;
    }

}
