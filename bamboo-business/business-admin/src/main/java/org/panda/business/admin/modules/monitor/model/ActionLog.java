package org.panda.business.admin.modules.monitor.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * 操作日志实体
 *
 * @author fangen
 * @since JDK 11 2022/5/6
 */
@Setter
@Getter
public class ActionLog {
    private static final long serialVersionUID = 2345642021345752148L;

    private BigInteger id;

    private Integer actionType;

    // 视图字段
    private String actionDesc;

    private String content;

    private String ipAddress;

    private String username;

    private Date operatingTime;

    private String operatingTimeStr;

    private Long elapsedTime;

    private Integer statusCode;

    private String exceptionInfo;
}
