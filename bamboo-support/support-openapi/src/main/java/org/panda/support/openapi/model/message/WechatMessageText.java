package org.panda.support.openapi.model.message;

/**
 * 微信消息文本
 *
 * @author fangen
 */
public class WechatMessageText {

    private String value;
    private String color;

    public WechatMessageText() {
    }

    public WechatMessageText(String value) {
        this(value, "#000000");
    }

    public WechatMessageText(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
