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
    private String parentLocation;
    /**
     * 实体类路径
     */
    private String entityLocation;
    /**
     * 数据访问层路径
     */
    private String repositoryLocation;
    /**
     * 服务层路径
     */
    private String serviceLocation;

    /**
     * mapperXml层路径
     */
    private String mapperXmlLocation;
}
