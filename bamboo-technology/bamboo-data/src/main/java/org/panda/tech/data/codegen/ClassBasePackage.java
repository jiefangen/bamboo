package org.panda.tech.data.codegen;

import lombok.Getter;
import lombok.Setter;

/**
 * 生成类文件位置
 *
 * @author fangen
 **/
@Getter
@Setter
public class ClassBasePackage {
    /**
     * 父级路径
     */
    private String parentPackage;
    /**
     * 实体类路径
     */
    private String entityPackage;
    /**
     * 数据访问层路径
     */
    private String repositoryPackage;
    /**
     * 服务层路径
     */
    private String servicePackage;

    /**
     * 数据库连接密码
     */
    private String datasourcePassword;

}
