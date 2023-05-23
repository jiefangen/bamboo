package org.panda.tech.core.web.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("bamboo.web.security")
public class WebSecurityProperties {

    private boolean csrfEnabled;
    private List<String> ignoringPatterns;

    public boolean isCsrfEnabled() {
        return this.csrfEnabled;
    }

    public void setCsrfEnabled(boolean csrfEnabled) {
        this.csrfEnabled = csrfEnabled;
    }

    public List<String> getIgnoringPatterns() {
        return this.ignoringPatterns;
    }

    public void setIgnoringPatterns(List<String> ignoringPatterns) {
        this.ignoringPatterns = ignoringPatterns;
    }

}
