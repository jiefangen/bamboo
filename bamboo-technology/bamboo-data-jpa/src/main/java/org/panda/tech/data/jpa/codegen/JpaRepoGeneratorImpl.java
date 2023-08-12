package org.panda.tech.data.jpa.codegen;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.data.codegen.ClassGeneratorSupport;
import org.panda.tech.data.model.entity.Entity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA Repo类生成器实现
 *
 * @author fangen
 */
public class JpaRepoGeneratorImpl extends ClassGeneratorSupport implements JpaRepoGenerator {

    private String baseTemplateLocation = "META-INF/templates/jpa-repo.ftl";
    private String extTemplateLocation = "META-INF/templates/jpa-repox.ftl";

    private Class<?>[] ignoredEntityClasses;

    public JpaRepoGeneratorImpl(String modelBasePackage, String targetBasePackage, Class<?>[] ignoredEntityClasses) {
        super(modelBasePackage, targetBasePackage);
        this.ignoredEntityClasses = ignoredEntityClasses;
    }

    @Override
    public void generate(String... modules) {
        generate(this.modelBasePackage, (module, entityClass) -> {
            if (this.ignoredEntityClasses == null || !ArrayUtils.contains(this.ignoredEntityClasses, entityClass)) {
                try {
                    generateRepo(module, entityClass, false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, modules);
    }

    @Override
    public void generate(Class<? extends Entity> entityClass, boolean withRepox) throws Exception {
        String module = getModule(entityClass);
        generateRepo(module, entityClass, withRepox);
    }

    private void generateRepo(String module, Class<? extends Entity> entityClass, boolean withRepox) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Field keyField = ClassUtil.findField(entityClass, "id");
        Class<?> keyFieldType = keyField.getType();
        params.put("keyClassSimpleName", keyFieldType.getSimpleName());
        generateRepo(module, entityClass, params, this.baseTemplateLocation, Strings.EMPTY);
        if (withRepox) { // 生成数据库访问仓库扩展类
            generateRepo(module, entityClass, params, this.extTemplateLocation, "x");
        }
    }

    private String generateRepo(String module, Class<? extends Entity> entityClass, Map<String, Object> params,
            String location, String repoClassNameSuffix) throws IOException {
        String packageName = getTargetModulePackageName(module);
        String entityClassSimpleName = entityClass.getSimpleName();
        String repoClassSimpleName = entityClassSimpleName + "Repo" + repoClassNameSuffix;
        String repoClassName = packageName + Strings.DOT + repoClassSimpleName;
        params.put("packageName", packageName);
        params.put("entityClassName", entityClass.getName());
        params.put("repoClassSimpleName", repoClassSimpleName);
        params.put("entityClassSimpleName", entityClassSimpleName);
        generate(repoClassName, location, params);
        return repoClassSimpleName;
    }

}
