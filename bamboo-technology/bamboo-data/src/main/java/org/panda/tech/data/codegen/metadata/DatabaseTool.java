package org.panda.tech.data.codegen.metadata;

import org.panda.bamboo.common.util.clazz.ClassDefault;
import org.panda.bamboo.common.util.clazz.ClassParse;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 *
 * @author fangen
 **/
public class DatabaseTool {
    public static final String COMMON_YES = "yes";
    public static final String COMMON_NO = "no";

    public static List<ColumnMetaData> getColumnMetadata(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<ColumnMetaData> columnMetadataList = new ArrayList<>();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        while (columns.next()) {
            ColumnMetaData columnMetaData = new ColumnMetaData();
            columnMetaData.setColumnName(columns.getString("COLUMN_NAME"));
            columnMetaData.setDataType(columns.getInt("DATA_TYPE"));
            columnMetaData.setTypeName(columns.getString("TYPE_NAME"));
            columnMetaData.setColumnSize(columns.getInt("COLUMN_SIZE"));
            columnMetaData.setAutoIncrement(getBoolean(columns.getString("IS_AUTOINCREMENT")));
            columnMetaData.setNullable(getBoolean(columns.getString("IS_NULLABLE")));
            columnMetaData.setColumnDefault(columns.getString("COLUMN_DEF"));
            columnMetaData.setRemarks(columns.getString("REMARKS"));
            columnMetadataList.add(columnMetaData);
        }
        columns.close();
        return columnMetadataList;
    }

    private static Boolean getBoolean(String value) {
        if (COMMON_YES.equalsIgnoreCase(value)) {
            return true;
        } else if (COMMON_NO.equalsIgnoreCase(value)) {
            return false;
        }
        return ClassDefault.visit(boolean.class, false);
    }

    /**
     * 数据库类型转Java类型
     */
    public static String convertJavaType(int dataType) {
        switch (dataType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return ClassParse.getClassTypeName(Boolean.class);
            case Types.TINYINT:
                return ClassParse.getClassTypeName(Byte.class);
            case Types.SMALLINT:
                return ClassParse.getClassTypeName(Short.class);
            case Types.INTEGER:
                return ClassParse.getClassTypeName(Integer.class);
            case Types.BIGINT:
                return ClassParse.getClassTypeName(Long.class);
            case Types.REAL:
                return ClassParse.getClassTypeName(Float.class);
            case Types.FLOAT:
            case Types.DOUBLE:
                return ClassParse.getClassTypeName(Double.class);

            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return ClassParse.getClassTypeName(String.class);

            case Types.NUMERIC:
            case Types.DECIMAL:
                return ClassParse.getClassTypeName(BigDecimal.class);
            case Types.DATE:
                return ClassParse.getClassTypeName(LocalDate.class);
            case Types.TIME:
                return ClassParse.getClassTypeName(LocalTime.class);
            case Types.TIMESTAMP:
                return ClassParse.getClassTypeName(LocalDateTime.class);
            default:
                return null;
        }
    }

    /**
     * 数据库类型转Java类型
     * 非基本数据类型导入类型收集
     */
    public static String getColumnJavaType(int dataType, List<String> importJavaTypes) {
        switch (dataType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return ClassParse.getClassTypeName(Boolean.class);
            case Types.TINYINT:
                return ClassParse.getClassTypeName(Byte.class);
            case Types.SMALLINT:
                return ClassParse.getClassTypeName(Short.class);
            case Types.INTEGER:
                return ClassParse.getClassTypeName(Integer.class);
            case Types.BIGINT:
                return ClassParse.getClassTypeName(Long.class);
            case Types.REAL:
                return ClassParse.getClassTypeName(Float.class);
            case Types.FLOAT:
            case Types.DOUBLE:
                return ClassParse.getClassTypeName(Double.class);
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return ClassParse.getClassTypeName(String.class);

            case Types.NUMERIC:
            case Types.DECIMAL:
                importJavaTypes.add(ClassParse.getClassType(BigDecimal.class));
                return ClassParse.getClassTypeName(BigDecimal.class);
            case Types.DATE:
                importJavaTypes.add(ClassParse.getClassType(LocalDate.class));
                return ClassParse.getClassTypeName(LocalDate.class);
            case Types.TIME:
                importJavaTypes.add(ClassParse.getClassType(LocalTime.class));
                return ClassParse.getClassTypeName(LocalTime.class);
            case Types.TIMESTAMP:
                importJavaTypes.add(ClassParse.getClassType(LocalDateTime.class));
                return ClassParse.getClassTypeName(LocalDateTime.class);
            default:
                return null;
        }
    }

}
