package org.panda.service.notice.core.domain.model.email;

import lombok.Setter;
import org.panda.bamboo.common.constant.basic.Strings;

/**
 * 邮件源
 */
@Setter
public class EmailSource {
    /**
     * 发件人地址
     */
    private String address;
    /**
     * 发件人名称
     */
    private String name;
    /**
     * 邮件字符编码
     */
    private String encoding = Strings.ENCODING_UTF8;

    public EmailSource() {
    }

    /**
     * 用默认的字符编码（{@link Strings#ENCODING_UTF8}）构建邮件源
     *
     * @param address 发件人地址
     * @param name    发件人名称
     */
    public EmailSource(String address, String name) {
        this.address = address;
        this.name = name;
    }

    /**
     * 构建邮件源
     *
     * @param address  发件人地址
     * @param name     发件人名称
     * @param encoding 邮件字符编码
     */
    public EmailSource(String address, String name, String encoding) {
        this.address = address;
        this.name = name;
        this.encoding = encoding;
    }

    /**
     * @return 发件人地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @return 发件人名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return 邮件字符编码
     */
    public String getEncoding() {
        return this.encoding;
    }

}
