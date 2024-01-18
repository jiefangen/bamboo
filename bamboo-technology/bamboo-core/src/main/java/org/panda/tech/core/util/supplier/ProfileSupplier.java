package org.panda.tech.core.util.supplier;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.core.util.SpringUtil;
import org.panda.tech.core.config.ProfileProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 供应者：获取当前profile
 */
@Component
public class ProfileSupplier implements Supplier<String>, ApplicationContextAware {

    private String profile = Strings.EMPTY; // 默认为空，表示无profile区分

    @Autowired
    private ProfileProperties profileProperties;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        String profile = SpringUtil.getActiveProfile(context);
        if (profile != null) {
            this.profile = profile;
        }
    }

    @Override
    public String get() {
        return this.profile;
    }

    public boolean isFormal() {
        return this.profileProperties.isFormal();
    }
}
