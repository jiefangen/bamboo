package org.panda.support.security.model;

import lombok.Data;

import java.util.List;

/**
 * 服务节点视图
 *
 * @author fangen
 **/
@Data
public class ServiceNodeVO {
    /**
     * 应用服务名称
     */
    private String appName;
    /**
     * 应用服务编码
     */
    private String appCode;
    /**
     * 应用运行环境
     */
    private Integer env;
    /**
     * 服务网关URI
     */
    private String gatewayUri;
    /**
     * 节点访问URI集合
     */
    private List<String> directUris;
}
