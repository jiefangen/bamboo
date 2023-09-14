package org.panda.service.notice.core.domain.model.email;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 邮件消息
 */
public class EmailMessage implements Serializable {

    private static final long serialVersionUID = 7813451258043674039L;

    /**
     * 收件人地址清单
     */
    private String[] addresses;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 构建邮件消息
     *
     * @param addresses 收件人地址清单
     * @param title     标题
     * @param content   内容
     */
    public EmailMessage(String[] addresses, String title, String content) {
        this.addresses = addresses;
        this.title = title;
        this.content = content;
    }

    /**
     * 构建邮件消息
     *
     * @param address 单个收件人地址
     * @param title   标题
     * @param content 内容
     */
    public EmailMessage(String address, String title, String content) {
        this.addresses = new String[]{ address };
        this.title = title;
        this.content = content;
    }

    /**
     * @return 收件人地址清单
     */
    public String[] getAddresses() {
        return this.addresses;
    }

    /**
     * @return 标题
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return 内容
     */
    public String getContent() {
        return this.content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailMessage that = (EmailMessage) o;
        return Arrays.equals(this.addresses, that.addresses)
                && this.title.equals(that.title)
                && Objects.equals(this.content, that.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.title, this.content);
        result = 31 * result + Arrays.hashCode(this.addresses);
        return result;
    }

}
