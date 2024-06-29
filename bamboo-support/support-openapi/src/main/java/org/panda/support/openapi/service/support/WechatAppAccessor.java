package org.panda.support.openapi.service.support;

import org.panda.support.openapi.model.WechatUser;
import org.panda.support.openapi.service.exception.WechatOpenApiExceptionCodes;
import org.panda.tech.core.exception.business.BusinessException;

/**
 * 微信应用访问器
 *
 * @author fangen
 */
public interface WechatAppAccessor {

    /**
     * 根据登录编码获取微信用户信息
     *
     * @param loginCode 登录编码
     * @return 微信用户信息，如果登录编码无效则返回null
     */
    WechatUser getUser(String loginCode);

    /**
     * 根据登录编码加载微信用户信息<br>
     * 注意：一个登录编码通过本方法加载一次后将失效，这意味着本方法不是幂等的
     *
     * @param loginCode 登录编码
     * @return 微信用户信息
     * @throws BusinessException 如果登录编码无效
     */
    default WechatUser loadUser(String loginCode) {
        WechatUser user = getUser(loginCode);
        if (user == null) { // 无效的微信登录编码
            throw new BusinessException(WechatOpenApiExceptionCodes.INVALID_LOGIN_MSG);
        }
        return user;
    }

}
