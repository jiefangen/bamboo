package org.panda.tech.data.codegen;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.parser.FreeMarkerTemplateParser;
import org.panda.bamboo.common.parser.TemplateParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * 类生成器支持
 *
 * @author fangen
 */
public abstract class ClassGeneratorSupport extends ModelBasedGeneratorSupport {

    private String targetBasePackage;
    private TemplateParser templateParser = new FreeMarkerTemplateParser();

    public ClassGeneratorSupport(String modelBasePackage, String targetBasePackage) {
        super(modelBasePackage);
        this.targetBasePackage = targetBasePackage;
    }

    public void setTemplateParser(TemplateParser templateParser) {
        this.templateParser = templateParser;
    }

    protected String getTargetModulePackageName(String module) {
        String packageName = this.targetBasePackage;
        if (StringUtils.isNotBlank(module)) {
            packageName += Strings.DOT + module;
        }
        return packageName;
    }

    protected void generate(String className, String templateLocation, Map<String, Object> params)
            throws IOException {
        if (templateLocation != null) {
            try {
                ClassUtils.forName(className, null);
            } catch (ClassNotFoundException e) {
                File template = new ClassPathResource(templateLocation).getFile();
                String content = this.templateParser.parse(template, params, Locale.getDefault());
                File file = getJavaFile(className);
                FileWriter writer = new FileWriter(file);
                IOUtils.write(content, writer);
                writer.close();
            }
        }
    }

    private File getJavaFile(String className) throws IOException {
        String projectPath = System.getProperty("user.dir"); // 运行项目工程根
        String path = "src/main/java/" + className.replaceAll("[.]", Strings.SLASH) + ".java";
        String pathname = projectPath + Strings.SLASH + path;
        File file = new File(pathname);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    protected void putClassName(Map<String, Object> params, Class<?> clazz, String paramNamePrefix) {
        params.put(paramNamePrefix + "ClassSimpleName", clazz.getSimpleName());
        String className = getImportedClassName(clazz);
        if (className != null) {
            params.put(paramNamePrefix + "ClassName", className);
        }
    }

    private String getImportedClassName(Class<?> clazz) {
        if (clazz.isPrimitive() || "java.lang".equals(clazz.getPackageName())) {
            return null;
        }
        return clazz.getName();
    }

}
