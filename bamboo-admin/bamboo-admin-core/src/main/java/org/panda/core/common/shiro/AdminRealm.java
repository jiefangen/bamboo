package org.panda.core.common.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.panda.core.modules.system.dao.UserDao;
import org.panda.core.modules.system.domain.po.RolePO;
import org.panda.core.modules.system.domain.po.UserPO;
import org.panda.core.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiefangen
 * @since JDK 1.8  2020/5/11
 **/
public class AdminRealm extends AuthorizingRealm{

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

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
            throw new AuthenticationException("Username is empty,Login failed!");
        }
        String username = authenticationToken.getPrincipal().toString();

        // 通过用户名在数据库查到该用户的信息
        UserPO user = userDao.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("Username does not exist,Login failed!");
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
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
        String username = (String) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<RolePO> roles = userService.getUserAndRoles(username).getRoles();
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
