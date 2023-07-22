package org.panda.tech.core.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Web操作日志参量范围
 **/
@Data
public class WebLogRange implements Serializable {

    private static final long serialVersionUID = 2808451047698815202L;

    /**
     * 记录开始时间戳
     */
    private Long startTimeMillis;
    /**
     * 主机地址
     */
    private String host;
    /**
     * IP归属地址
     */
    private String ipAddress;
    /**
     * 操作类型
     */
    private String actionType;
    /**
     * 默认操作内容
     */
    private String content;
    /**
     * 访问者身份
     */
    private String identity;
    /**
     * 请求参数体
     */
    private String bodyStr;
    /**
     * 花费时长
     */
    private Long takeTime;
}
