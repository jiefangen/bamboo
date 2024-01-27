package org.panda.tech.security.cas.logout;

import org.panda.tech.core.web.config.meta.ApiMetaProperties;
import org.panda.tech.security.web.SecurityUrlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * CAS登出成功处理器
 */
public abstract class CasLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    public CasLogoutSuccessHandler(SecurityUrlProvider urlProvider) {
        String logoutSuccessUrl = urlProvider.getLogoutSuccessUrl();
        if (logoutSuccessUrl != null) {
            setDefaultTargetUrl(logoutSuccessUrl);
        }
    }

    @Override
    @Autowired // 覆写以自动注入
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        super.setRedirectStrategy(redirectStrategy);
    }

    @Autowired
    public void setApiMetaProperties(ApiMetaProperties apiMetaProperties) {
        setTargetUrlParameter(apiMetaProperties.getRedirectTargetUrlParameter());
    }

}
