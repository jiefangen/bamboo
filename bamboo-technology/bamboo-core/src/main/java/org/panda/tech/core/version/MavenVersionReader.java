package org.panda.tech.core.version;

import org.springframework.context.ApplicationContext;

/**
 * Maven版本号读取器
 */
public class MavenVersionReader extends AbstractVersionReader {

    @Override
    protected String readFullVersion(ApplicationContext context) {
        return context.getEnvironment().getProperty("project.version");
    }
}
