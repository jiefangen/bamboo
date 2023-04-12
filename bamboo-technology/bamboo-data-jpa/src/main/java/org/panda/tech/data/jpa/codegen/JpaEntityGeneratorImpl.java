package org.panda.tech.data.jpa.codegen;

import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassGeneratorSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void generate(String tableName) throws Exception {
        if (tableName.startsWith("t_")) {
            tableName = tableName.substring(1);
        }
        String entityName = StringUtil.underLineToCamelCase(tableName, false);
        String tableName1 = StringUtil.prependUnderLineToUpperChar(entityName, true);
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

        ResultSetMetaData setMetaData = rs.getMetaData();
        int columnCount = setMetaData.getColumnCount();
        List<String> fields = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            fields.add(setMetaData.getColumnName(i));
            types.add(setMetaData.getColumnClassName(i));
        }

        Map<String, Object> params = new HashMap<>();
        generateEntity(Strings.EMPTY, entityName, fields, types, params, this.templateLocation);
        rs.close();
        connection.close();
    }

    private String generateEntity(String module, String className, List<String> fields, List<String> types,
                            Map<String, Object> params, String location) throws IOException {
        String packageName = getTargetModulePackageName(module);
        String entityClassName = packageName + Strings.DOT + className;
        params.put("packageName", packageName);
        params.put("className", className);
        params.put("fields", fields);
        params.put("types", types);
        generate(entityClassName, location, params);
        return className;
    }

}
