package org.panda.service.notice.core.domain.single.sms.content;

import org.panda.bamboo.common.constant.Profiles;
import org.panda.bamboo.core.context.ApplicationContextBean;

/**
 * 抽象的短信内容发送器
 */
public abstract class AbstractSmsContentSender implements SmsContentSender {

    private String[] types;

    @Override
    public String[] getTypes() {
        return this.types;
    }

    public void setTypes(String... types) {
        this.types = types;
    }

    @Override
    public int getIntervalSeconds() {
        String profile = ApplicationContextBean.getActiveProfile();
        switch (profile) {
            case Profiles.JUNIT:
            case Profiles.LOCAL:
            case Profiles.DEV:
                return 10;
            case Profiles.TEST:
                return 30;
        }
        return 60;
    }

}
