package org.panda.tech.data.jpa.codegen;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.exception.business.param.RequiredParamException;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassGeneratorSupport;
import org.panda.tech.data.codegen.metadata.DatabaseTool;
import org.panda.tech.data.codegen.metadata.ColumnMetaData;
import org.panda.tech.data.codegen.metadata.FieldMetaData;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JPA实体文件生成器实现
 *
 * @author fangen
 */
public class JpaEntityGeneratorImpl extends ClassGeneratorSupport implements JpaEntityGenerator {

    private String templateLocation = "META-INF/templates/jpa-entity.ftl";

    public JpaEntityGeneratorImpl(String modelBasePackage, String targetBasePackage) {
        super(modelBasePackage, targetBasePackage);
    }

    @Override
    public void generate(String... tableOrEntityNames) throws Exception {
        if (ArrayUtils.isEmpty(tableOrEntityNames)) {
            throw new RequiredParamException();
        }
        for (String tableOrEntityName : tableOrEntityNames) {
            generateEntity(tableOrEntityName);
        }
    }

    @Override
    public void generate(String tableOrEntityName) throws Exception {
        if (StringUtils.isEmpty(tableOrEntityName)) {
            throw new RequiredParamException();
        }
        generateEntity(tableOrEntityName);
    }

    private void generateEntity(String tableOrEntityName) throws Exception {
        String tableName;
        String entityName;
        if (tableOrEntityName.contains(Strings.UNDERLINE)) { // 数据库表名
            tableName = tableOrEntityName;
            if (tableOrEntityName.startsWith(Commons.TABLE_PREFIX)) { // 去掉表名t_前缀转换实体
                tableOrEntityName = tableOrEntityName.substring(2);
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

        List<ColumnMetaData> columnMetaDataList = DatabaseTool.getColumnMetadata(metaData, tableName);
        List<FieldMetaData> fieldMetaDataList = new ArrayList<>();
        List<String> importJavaTypes = new ArrayList<>();
        for (ColumnMetaData columnMetaData : columnMetaDataList) {
            FieldMetaData fieldMetaData = new FieldMetaData();
            fieldMetaData.transform(columnMetaData);

            String fieldName = StringUtil.underLineToCamelCase(columnMetaData.getColumnName(), true);
            fieldMetaData.setFieldName(fieldName);

            String fieldTypeName = DatabaseTool.getColumnJavaType(columnMetaData.getDataType(), importJavaTypes);
            fieldMetaData.setFieldTypeName(fieldTypeName);
            fieldMetaDataList.add(fieldMetaData);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("fieldMetaDataList", fieldMetaDataList);
        params.put("importJavaTypes", importJavaTypes.stream().distinct().collect(Collectors.toList()));
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

}
