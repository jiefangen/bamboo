package org.panda.tech.data.codegen.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库映射元数据扩展列属性
 *
 * @author fangen
 **/
@Getter
@Setter
public class FieldMetaData extends ColumnMetaData {

    private String fieldType;
    private String fieldName;
    private String fieldTypeName;

    public void transform(ColumnMetaData columnMetaData) {
        this.setColumnName(columnMetaData.getColumnName());
        this.setDataType(columnMetaData.getDataType());
        this.setTypeName(columnMetaData.getTypeName());
        this.setColumnSize(columnMetaData.getColumnSize());
        this.setAutoIncrement(columnMetaData.getAutoIncrement());
        this.setNullable(columnMetaData.getNullable());
        this.setRemarks(columnMetaData.getRemarks());
        this.setColumnDefault(columnMetaData.getColumnDefault());
    }

}
