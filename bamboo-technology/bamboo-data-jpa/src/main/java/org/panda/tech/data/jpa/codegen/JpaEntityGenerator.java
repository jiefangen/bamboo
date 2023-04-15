package org.panda.tech.data.jpa.codegen;


/**
 * JPA实体映射文件生成器
 *
 * @author fangen
 */
public interface JpaEntityGenerator {

    void generate(String... tableOrEntityNames) throws Exception;

    void generate(String tableOrEntityName) throws Exception;

}
