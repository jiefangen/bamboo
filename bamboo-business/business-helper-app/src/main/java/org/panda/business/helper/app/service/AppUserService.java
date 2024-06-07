package org.panda.business.helper.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.helper.app.model.entity.AppUser;
import org.panda.business.helper.app.model.params.AppLoginParam;
import org.panda.tech.core.web.restful.RestfulResult;

/**
 * <p>
 * APP用户 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2024-06-05
 */
public interface AppUserService extends IService<AppUser> {

    RestfulResult<?> appLogin(AppLoginParam appLoginParam);
}
