package org.panda.tech.data.jpa.codegen;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.exception.param.RequiredParamException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.ClassDefault;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassGeneratorSupport;
import org.panda.tech.data.model.meta.ColumnMetaData;
import org.panda.tech.data.model.meta.FieldMetaData;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;

/**
 * JPA实体文件生成器实现
 *
 * @author fangen
 */
public class JpaEntityGeneratorImpl extends ClassGeneratorSupport implements JpaEntityGenerator {

    private String templateLocation = "META-INF/template/jpa-entity.ftl";

    public JpaEntityGeneratorImpl(String modelBasePackage, String targetBasePackage) {
        super(modelBasePackage, targetBasePackage);
    }

    @Override
    public void generate(String tableOrEntityName) throws Exception {
        if (StringUtils.isEmpty(tableOrEntityName)) {
            throw new RequiredParamException();
        }
        String tableName;
        String entityName;
        if (tableOrEntityName.contains(Strings.UNDERLINE)) { // 表名
            tableName = tableOrEntityName;
            if (tableOrEntityName.startsWith("t_")) { // 去掉表名t_前缀转换实体
                tableOrEntityName = tableOrEntityName.substring(1);
            }
            entityName = StringUtil.underLineToCamelCase(tableOrEntityName, false);
        } else { // 实体类名
            entityName = tableOrEntityName;
            tableName = StringUtil.prependUnderLineToUpperChar(entityName, true);
        }
        generateEntity(tableName, entityName);
    }

    private void generateEntity(String tableName, String entityName)  throws Exception{
        DataSource dataSource = ApplicationContextBean.getBean(DataSource.class);
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, tableName, new String[]{ "TABLE" });
        if (!rs.next()) {
            LogUtil.warn(getClass(), "====== Table {} does not exist, entity file can not been generated.", tableName);
            return;
        }

        List<ColumnMetaData> columnMetaDataList = getColumnMetadata(metaData, tableName);
        List<FieldMetaData> fieldMetaDataList = new ArrayList<>();
        for (ColumnMetaData columnMetaData : columnMetaDataList) {
            FieldMetaData fieldMetaData = new FieldMetaData();
            fieldMetaData.transform(columnMetaData);
            String fieldName = StringUtil.underLineToCamelCase(columnMetaData.getColumnName(), true);
            fieldMetaData.setFieldName(fieldName);
            String fieldTypeName = getColumnJavaType(columnMetaData.getDataType());
            fieldMetaData.setFieldTypeName(fieldTypeName);
            fieldMetaDataList.add(fieldMetaData);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("fieldMetaDataList", fieldMetaDataList);
        params.put("tableName", tableName);
        generateEntity(Strings.EMPTY, entityName, params, this.templateLocation);

        rs.close();
        connection.close();
    }

    private String generateEntity(String module, String entityName, Map<String, Object> params, String location) throws IOException {
        String packageName = getTargetModulePackageName(module);
        String entityClassName = packageName + Strings.DOT + entityName;
        params.put("packageName", packageName);
        params.put("entityName", entityName);
        generate(entityClassName, location, params);
        return entityName;
    }

    private List<ColumnMetaData> getColumnMetadata(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<ColumnMetaData> columnMetadataList = new ArrayList<>();
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        while (rs.next()) {
            ColumnMetaData columnMetaData = new ColumnMetaData();
            columnMetaData.setColumnName(rs.getString("COLUMN_NAME"));
            columnMetaData.setDataType(rs.getInt("DATA_TYPE"));
            columnMetaData.setTypeName(rs.getString("TYPE_NAME"));
            columnMetaData.setColumnSize(rs.getInt("COLUMN_SIZE"));
            columnMetaData.setAutoIncrement(getBoolean(rs.getString("IS_AUTOINCREMENT")));
            columnMetaData.setNullable(getBoolean(rs.getString("IS_NULLABLE")));
            columnMetaData.setColumnDefault(rs.getString("COLUMN_DEF"));
            columnMetadataList.add(columnMetaData);
        }
        rs.close();
        return columnMetadataList;
    }

    private Boolean getBoolean(String value) {
        if ("yes".equalsIgnoreCase(value)) {
            return true;
        } else if ("no".equalsIgnoreCase(value)) {
            return false;
        }
        return ClassDefault.visit(boolean.class, false);
    }

    private String getColumnJavaType(int dataType) {
        switch (dataType) {
            case Types.CHAR:
                return String.class.getName();
            case Types.VARCHAR:
                return String.class.getName();
            case Types.LONGVARCHAR: // longtext
                return String.class.getName();
            case Types.TINYINT:
                return Integer.class.getName();
            case Types.INTEGER:
                return Integer.class.getName();
            case Types.BIGINT:
                return Long.class.getName();
            case Types.DATE:
                return Date.class.getName();
            case Types.TIMESTAMP:
                return LocalDateTime.class.getName();
            default:
                return null;
        }
    }

}
