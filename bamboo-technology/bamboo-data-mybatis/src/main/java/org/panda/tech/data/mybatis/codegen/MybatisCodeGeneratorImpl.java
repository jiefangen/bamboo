package org.panda.tech.data.mybatis.codegen;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.param.RequiredParamException;
import org.panda.tech.data.codegen.ClassBasePackage;
import org.panda.tech.data.datasource.DataSourceConnConfig;

/**
 * mybatis代码生成器实现
 *
 * @author fangen
 */
public class MybatisCodeGeneratorImpl extends MybatisGeneratorSupport implements MybatisCodeGenerator {

    private String templateLocation = "/templates/mapper.xml.ftl";

    private DataSourceConnConfig dataSourceConnConfig;

    public MybatisCodeGeneratorImpl(DataSourceConnConfig dataSourceConnConfig, ClassBasePackage classBasePackage) {
        super(classBasePackage);
        this.dataSourceConnConfig = dataSourceConnConfig;
    }

    @Override
    public void generate(String... tableNames) {
        if (ArrayUtils.isEmpty(tableNames)) {
            throw new RequiredParamException();
        }
        String tableName = String.join(",", tableNames);
        generate(tableName, true);
    }

    @Override
    public void generate(String tableName, boolean withService) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(dataSourceConnConfig.getUrl());
        dataSourceConfig.setDriverName(dataSourceConnConfig.getDriverName());
        dataSourceConfig.setUsername(dataSourceConnConfig.getUsername());
        dataSourceConfig.setPassword(dataSourceConnConfig.getPassword());
        generateCode(dataSourceConfig, tableName, withService);
    }

    private void generateCode(DataSourceConfig dataSourceConfig, String tableName, boolean withService) {
        // 默认生成的类名去掉t_前缀
        String tablePrefix = Commons.TABLE_PREFIX;
        generate(this.templateLocation, dataSourceConfig, tableName, withService, tablePrefix);
    }

}
