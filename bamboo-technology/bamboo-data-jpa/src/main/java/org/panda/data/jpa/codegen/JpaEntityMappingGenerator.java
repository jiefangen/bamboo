package org.panda.data.jpa.codegen;

import org.panda.data.model.entity.Entity;

/**
 * JPA实体映射文件生成器
 *
 * @author fangen
 */
public interface JpaEntityMappingGenerator {

    void generate(String... modules) throws Exception;

    void generate(Class<? extends Entity> entityClass, String tableName) throws Exception;

}
