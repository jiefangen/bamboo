package org.panda.service.notice.core.domain.single.email.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件提供者抽象实现
 */
public abstract class AbstractEmailProvider implements EmailProvider {
    /**
     * 邮件类型
     */
    private String type;
    /**
     * 邮件标题
     */
    protected String title;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
