package org.panda.business.admin.v1.infrastructure.security.bussiness;

import org.apache.commons.lang3.StringUtils;
import org.panda.tech.security.user.UserConfigAuthority;
import org.panda.tech.security.web.AuthoritiesBizExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限集业务扩展实现
 *
 * @author fangen
 **/
@Component
public class AuthoritiesBizExecutorImpl implements AuthoritiesBizExecutor {
    /**
     * 映射集：api路径-所需权限清单
     */
    private final Map<String, Collection<UserConfigAuthority>> apiConfigAuthoritiesMapping = new HashMap<>();

    @Override
    public void execute(String api, Collection<UserConfigAuthority> authorities) {
        if (StringUtils.isNotBlank(api)) {
            this.apiConfigAuthoritiesMapping.put(api, authorities);
        }
    }

    @Override
    public Map<String, Collection<UserConfigAuthority>> getApiConfigAuthoritiesMapping() {
        return this.apiConfigAuthoritiesMapping;
    }
}
