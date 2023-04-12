package org.panda.tech.data.support;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据访问模板工厂
 *
 * @author fangen
 */
@Component
public class DataAccessTemplateFactoryImpl implements DataAccessTemplateFactory, ContextInitializedBean {

    private Map<Class<?>, DataAccessTemplate> templateMappings = new HashMap<>();

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        context.getBeansOfType(DataAccessTemplate.class).values().forEach(template -> {
            template.getEntityClasses().forEach(entityClass -> {
                this.templateMappings.put(entityClass, template);
            });
        });
    }

    @Override
    public DataAccessTemplate getDataAccessTemplate(Class<?> entityClass) {
        return this.templateMappings.get(entityClass);
    }

}
