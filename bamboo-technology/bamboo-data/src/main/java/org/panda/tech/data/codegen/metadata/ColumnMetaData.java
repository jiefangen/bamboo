package org.panda.tech.data.codegen.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * 表字段元数据列属性
 *
 * @author fangen
 **/
@Getter
@Setter
public class ColumnMetaData {

    private String columnName;
    private Integer dataType;
    private String typeName;
    private Integer columnSize;
    private Boolean autoIncrement;
    private Boolean nullable;
    private String columnDefault;
    private String remarks;

}
