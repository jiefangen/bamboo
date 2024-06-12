package org.panda.business.helper.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.params.AppLoginParam;
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
    RestfulResult<?> appLogin(AppLoginParam appLoginParam);

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
}
