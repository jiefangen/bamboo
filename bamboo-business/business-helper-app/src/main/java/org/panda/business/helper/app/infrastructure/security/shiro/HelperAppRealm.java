package org.panda.business.helper.app.infrastructure.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.panda.business.helper.app.common.constant.ProjectConstants;

public class HelperAppRealm extends AuthorizingRealm {

//    @Autowired
//    private UserDao userDao;

    /**
     * 用户登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null) {
            throw new AuthenticationException(ProjectConstants.USER_EMPTY);
        }
        String username = authenticationToken.getPrincipal().toString();

        // 通过用户名在数据库查到该用户的信息
//        UserPO user = userDao.findByUsername(username);
//        if (user == null) {
//            LogUtil.warn(getClass(), SystemConstants.USERNAME_NOT_EXIST);
//            throw new UnknownAccountException(SystemConstants.USERNAME_NOT_EXIST);
//        } else {
//            if (user.getDisabled() != 0) {
//                throw new AccountException(SystemConstants.USER_DISABLED);
//            }
//
//            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
//                    ByteSource.Util.bytes(user.getSalt()), getName());
//            return simpleAuthenticationInfo;
//        }
        return new SimpleAuthenticationInfo();
    }

    /**
     * 系统接口鉴权认证
     * 该用户角色拥有相应的接口资源才可以访问
     *
     * @param principalCollection
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        UserPO user = (UserPO) principalCollection.getPrimaryPrincipal();
//        UserService userService = SpringContextHolder.getBean(UserService.class);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        List<RolePO> roles = userService.getUserAndRoles(user.getUsername()).getRoles();
        // 添加角色鉴权,对应使用注解@RequiresRoles()
//        Set<String> roleCodes = roles.stream().map(role -> role.getRoleCode()).collect(Collectors.toSet());
//        simpleAuthorizationInfo.setRoles(roleCodes);

        // TODO 添加权限鉴权
        // 每次访问带有权限限制的接口时就会验证，拥有对应权限code的话就可以正常访问。
//        simpleAuthorizationInfo.addStringPermissions(systemMenuList.stream()
//                .map(systemMenu -> systemMenu.getPermission())
//                .collect(Collectors.toList()));
        return simpleAuthorizationInfo;
    }

}
