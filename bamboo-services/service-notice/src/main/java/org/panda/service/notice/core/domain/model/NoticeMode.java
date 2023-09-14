package org.panda.service.notice.core.domain.model;

import org.panda.bamboo.common.annotation.Caption;
import org.panda.bamboo.common.annotation.EnumValue;

/**
 * 通知方式
 */
public enum NoticeMode {

    @Caption("短信通知")
    @EnumValue("S")
    SMS,

    @Caption("邮件通知")
    @EnumValue("E")
    EMAIL,

    @Caption("消息推送")
    @EnumValue("P")
    PUSH

}
