package org.panda.tech.data.jpa.codegen;


import org.panda.tech.data.model.entity.Entity;

/**
 * JPA Repo类生成器
 *
 * @author fangen
 */
public interface JpaRepoGenerator {

    void generate(String... modules) throws Exception;

    void generate(Class<? extends Entity> entityClass, boolean withRepox) throws Exception;

}
