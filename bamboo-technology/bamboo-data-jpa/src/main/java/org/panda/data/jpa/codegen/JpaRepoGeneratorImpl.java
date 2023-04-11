package org.panda.data.jpa.codegen;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.constant.Strings;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.data.codegen.ClassGeneratorSupport;
import org.panda.data.model.entity.Entity;

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

    private String baseTemplateLocation = "META-INF/template/jpa-repo.ftl";
    private String extTemplateLocation = "META-INF/template/jpa-repox.ftl";

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
                    generate(module, entityClass, true); // 默认均生成实现类，多余的可手工删除
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, modules);
    }

    @Override
    public void generate(Class<? extends Entity> entityClass, boolean withRepox) throws Exception {
        String module = getModule(entityClass);
        generate(module, entityClass, withRepox);
    }

    private void generate(String module, Class<? extends Entity> entityClass, boolean withRepox) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Field keyField = ClassUtil.findField(entityClass, "id");
        Class<?> keyFieldType = keyField.getType();
        params.put("keyClassSimpleName", keyFieldType.getSimpleName());
        generate(module, entityClass, params, this.baseTemplateLocation, Strings.EMPTY);
        if (withRepox) {
            generate(module, entityClass, params, this.extTemplateLocation, "x");
        }
    }

    private String generate(String module, Class<? extends Entity> entityClass, Map<String, Object> params,
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
