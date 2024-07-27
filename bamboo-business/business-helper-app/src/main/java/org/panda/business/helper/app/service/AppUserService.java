package org.panda.business.helper.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.business.helper.app.model.params.UpdateUserParam;
import org.panda.business.helper.app.model.vo.UserInfo;
import org.panda.tech.core.web.restful.RestfulResult;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * APP用户 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-05
 */
public interface AppUserService extends IService<AppUser> {
    /**
     * APP登录
     *
     * @param appLoginParam 登录参数
     * @return 登录结果
     */
    RestfulResult<?> appLogin(AppLoginParam appLoginParam, HttpServletRequest request);

    /**
     * APP登录
     *
     * @param appLoginParam 登录参数
     * @return 登录结果
     */
    RestfulResult<?> authAppLogin(AppLoginParam appLoginParam, HttpServletRequest request);

    /**
     * 登录验证
     *
     * @return 验证结果
     */
    RestfulResult<?> loginVerify(HttpServletRequest request);

    /**
     * APP登出
     *
     * @return 登出结果
     */
    RestfulResult<?> appLogout(HttpServletRequest request);

    /**
     * 根据token获取用户详情
     *
     * @return 用户详情
     */
    UserInfo getUserByToken(String token);

    /**
     * 更新用户
     *
     * @return 更新结果
     */
    boolean updateUser(UpdateUserParam updateUserParam, HttpServletRequest request);
}
