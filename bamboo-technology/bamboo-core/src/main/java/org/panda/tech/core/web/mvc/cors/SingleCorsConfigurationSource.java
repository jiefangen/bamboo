package org.panda.tech.core.web.mvc.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

/**
 * 单个配置的CORS配置源
 */
@Component
public class SingleCorsConfigurationSource implements CorsConfigurationSource {

    private CorsConfiguration corsConfiguration;

    public void setCorsConfiguration(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return this.corsConfiguration;
    }

}
