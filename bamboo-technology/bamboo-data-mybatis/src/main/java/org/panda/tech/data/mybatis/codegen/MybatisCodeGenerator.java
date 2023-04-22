package org.panda.tech.data.mybatis.codegen;


/**
 * JPA实体映射文件生成器
 *
 * @author fangen
 */
public interface MybatisCodeGenerator {

    void generate(String... tableNames) throws Exception;

    void generate(String tableName, boolean withService) throws Exception;

}
