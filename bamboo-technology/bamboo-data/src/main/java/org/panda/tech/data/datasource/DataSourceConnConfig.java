package org.panda.tech.data.datasource;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据源连接配置
 *
 * @author fangen
 **/
@Getter
@Setter
public class DataSourceConnConfig {
    /**
     * 数据库连接url
     */
    private String url;
    /**
     * 连接驱动名称
     */
    private String driverName;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
}
