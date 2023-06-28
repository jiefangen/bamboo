package org.panda.tech.core.web.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.annotation.Caption;

@Getter
@AllArgsConstructor
public enum LoginModeEnum {

    @Caption("用户名密码登录")
    ACCOUNT("account"),

    @Caption("短信登录")
    SMS("sms");

    private String value;

}
