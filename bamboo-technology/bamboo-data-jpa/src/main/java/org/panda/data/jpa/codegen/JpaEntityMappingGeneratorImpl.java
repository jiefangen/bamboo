package org.panda.data.jpa.codegen;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.XmlUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.data.codegen.ModelBasedGeneratorSupport;
import org.panda.data.model.entity.Entity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * JPA实体映射文件生成器实现
 *
 * @author fangen
 */
public class JpaEntityMappingGeneratorImpl extends ModelBasedGeneratorSupport implements JpaEntityMappingGenerator {

    @Autowired
    private DataSource dataSource;

    private String templateLocation = "META-INF/template/entity-mapping.xml";
    private String targetLocation = "META-INF/jpa/";

    private final Logger logger = LogUtil.getLogger(getClass());

    public JpaEntityMappingGeneratorImpl(String modelBasePackage) {
        super(modelBasePackage);
    }

    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    public void setTargetLocation(String targetLocation) {
        // 确保以/结尾
        if (!targetLocation.endsWith(Strings.SLASH)) {
            targetLocation += Strings.SLASH;
        }
        this.targetLocation = targetLocation;
    }

    @Override
    public void generate(String... modules) throws Exception {
        generate(this.modelBasePackage, (module, entityClass) -> {
            try {
                String tableName = "t_" + StringUtil.prependUnderLineToUpperChar(entityClass.getSimpleName(), true);
                generate(module, entityClass, tableName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, modules);
    }

    @Override
    public void generate(Class<? extends Entity> entityClass, String tableName) throws Exception {
        String module = getModule(entityClass);
        generate(module, entityClass, tableName);
    }

    private void generate(String module, Class<? extends Entity> entityClass, String tableName) throws Exception {
        ClassPathResource resource = getMappingResource(module, entityClass);
        if (resource != null) {
            Connection connection = DataSourceUtils.getConnection(this.dataSource);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, new String[]{ "TABLE" });
            if (!rs.next()) {
                this.logger.warn("====== Table {} does not exist, entity mapping file for {} can not been generated.",
                        tableName, entityClass.getName());
                return;
            }

            Document doc = readTemplate();
            Element rootElement = doc.getRootElement();
            Element entityElement = rootElement.addElement("entity")
                    .addAttribute("class", entityClass.getName());
            entityElement.addElement("table").addAttribute("name", tableName);
            Element attributesElement = entityElement.addElement("attributes");

            Field[] fields = entityClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (supports(field)) {
                    Class<?> fieldType = field.getType();
                    if (i == 0) { // 首个字段视为主键
                        Element idElement = attributesElement.addElement("id")
                                .addAttribute("name", field.getName());
                        JpaEntityColumn column = getColumn(metaData, tableName, field, null);
                        if (Boolean.TRUE.equals(column.getAutoIncrement())) {
                            idElement.addElement("generated-value").addAttribute("strategy", "IDENTITY");
                        }
                    }
                }
            }

            File file = getSourceFile(resource);
            XmlUtil.write(doc, file);
            connection.close();
        }
    }

    private void addConvertElement(Element parentElement, String converter) {
        if (converter != null) {
            parentElement.addElement("convert").addAttribute("converter", converter);
        }
    }

    private ClassPathResource getMappingResource(String module, Class<?> clazz) {
        ClassPathResource dir = new ClassPathResource(this.targetLocation);
        if (module != null) {
            dir = (ClassPathResource) dir.createRelative(module + Strings.SLASH);
        }
        String filename = clazz.getSimpleName() + ".xml";
        ClassPathResource resource = (ClassPathResource) dir.createRelative(filename);
        if (resource.exists()) {
            this.logger.info("====== Entity mapping file for {} already exists, which is ignored.",
                    clazz.getName());
            return null;
        }
        return resource;
    }

    private Document readTemplate() throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        ClassPathResource template = new ClassPathResource(this.templateLocation);
        InputStream in = template.getInputStream();
        Document doc = reader.read(in);
        in.close();
        return doc;
    }

    private boolean supports(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }


    private JpaEntityColumn getColumn(DatabaseMetaData metaData, String tableName, Field field, String columnNamePrefix)
            throws SQLException {
        String columnName = StringUtil.prependUnderLineToUpperChar(field.getName(), true);
        if (columnNamePrefix != null) {
            columnName = columnNamePrefix + columnName;
        }
        JpaEntityColumn column = new JpaEntityColumn(columnName);
        ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
        if (rs.next()) {
            Class<?> fieldType = field.getType();
            column.setAutoIncrement(getBoolean(rs.getString("IS_AUTOINCREMENT")));
            if (!fieldType.isPrimitive()) {
                column.setNullable(getBoolean(rs.getString("IS_NULLABLE")));
            }
            if (fieldType == String.class) { // 字段类型为字符串，则需获取其长度
                column.setLength(rs.getInt("COLUMN_SIZE"));
            }
            column.setDefinition(getColumnDefinition(fieldType, rs.getInt("DATA_TYPE")));
            if (fieldType == BigDecimal.class || fieldType == double.class || fieldType == float.class) { // 小数需获取精度
                column.setPrecision(rs.getInt("COLUMN_SIZE"));
                column.setScale(rs.getInt("DECIMAL_DIGITS"));
            }
        } else {
            column.setExists(false);
        }
        return column;
    }

    private Boolean getBoolean(String value) {
        if ("yes".equalsIgnoreCase(value)) {
            return true;
        } else if ("no".equalsIgnoreCase(value)) {
            return false;
        } else {
            return null;
        }
    }

    private String getColumnDefinition(Class<?> fieldType, int dataType) {
        switch (dataType) {
            case Types.CHAR:
                return "char";
            case Types.DATE:
                return "date";
            case Types.TIME:
                if (fieldType == Instant.class || fieldType == LocalDateTime.class) {
                    return null;
                }
                return "time";
            default:
                return null;
        }
    }

    private File getSourceFile(ClassPathResource resource) throws IOException {
        File root = new ClassPathResource("/").getFile(); // classpath根
        root = root.getParentFile().getParentFile(); // 工程根
        return new File(root, "src/main/resources/" + resource.getPath());
    }

}
