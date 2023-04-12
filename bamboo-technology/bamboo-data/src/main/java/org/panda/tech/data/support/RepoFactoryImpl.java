package org.panda.tech.data.support;

import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.tech.data.Repox;
import org.panda.tech.data.model.entity.Entity;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository工厂实现
 *
 * @author fangen
 */
@Component
public class RepoFactoryImpl implements RepoFactory, ContextInitializedBean {

    private Map<Class<?>, Repository<?, ?>> repositoryMapping = new HashMap<>();
    private Map<Class<?>, Repox<?>> repoxMapping = new HashMap<>();

    @Override
    @SuppressWarnings("rawtypes")
    public void afterInitialized(ApplicationContext context) throws Exception {
        Map<String, Repository> repositories = context.getBeansOfType(Repository.class);
        for (Repository<?, ?> repository : repositories.values()) {
            Class<?> entityClass = getEntityClass(repository);
            if (entityClass != null) {
                this.repositoryMapping.put(entityClass, repository);
            }
        }

        Map<String, Repox> beans = context.getBeansOfType(Repox.class);
        for (Repox<?> repox : beans.values()) {
            Class<?> entityClass = getEntityClass(repox);
            if (entityClass != null) {
                this.repoxMapping.put(entityClass, repox);
            }
        }
    }

    private Class<?> getEntityClass(Repository<?, ?> repository) {
        Class<?>[] interfaces = repository.getClass().getInterfaces();
        for (Class<?> clazz : interfaces) {
            if (clazz != Repository.class && Repository.class.isAssignableFrom(clazz)) {
                return ClassUtil.getActualGenericType(clazz, Repository.class, 0);
            }
        }
        return null;
    }

    private Class<?> getEntityClass(Repox<?> repox) {
        Class<?> entityClass = getEntityClassOfRepoClass(repox.getClass());
        if (entityClass == null && repox instanceof Advised) {
            entityClass = getEntityClassOfRepoClass(((Advised) repox).getTargetClass());
        }
        return entityClass;
    }

    private Class<?> getEntityClassOfRepoClass(Class<?> repoClass) {
        Class<?>[] interfaces = repoClass.getInterfaces();
        for (Class<?> clazz : interfaces) {
            if (clazz != Repox.class && Repox.class.isAssignableFrom(clazz)) {
                return ClassUtil.getActualGenericType(clazz, Repox.class, 0);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends CrudRepository<T, K>, T extends Entity, K> R getRepository(Class<T> entityClass) {
        return (R) this.repositoryMapping.get(entityClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Repox<T>, T extends Entity> R getRepox(Class<T> entityClass) {
        return (R) this.repoxMapping.get(entityClass);
    }

}
