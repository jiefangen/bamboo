package org.panda.tech.core.version;

import org.panda.tech.core.util.supplier.ProfileSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 抽象的版本号读取器
 */
public abstract class AbstractVersionReader implements VersionReader, ApplicationContextAware {

    private Version version;

    @Autowired
    private ProfileSupplier profileSupplier;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        String fullVersion = readFullVersion(context);
        this.version = new Version(fullVersion);
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public String getVersionText() {
        boolean formalProfile = this.profileSupplier.isFormal();
        return this.version.toText(!formalProfile);
    }

    /**
     * 读取完整版本号字符串
     *
     * @param context Spring容器上下文
     * @return 完整版本号字符串
     */
    protected abstract String readFullVersion(ApplicationContext context);


}
