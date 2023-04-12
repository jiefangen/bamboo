package org.panda.data.jpa.codegen;


/**
 * JPA实体映射文件生成器
 *
 * @author fangen
 */
public interface JpaEntityGenerator {

    void generate(String entityName) throws Exception;

}
