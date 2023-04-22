package org.panda.tech.data.mybatis.codegen;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.exception.param.RequiredParamException;
import org.panda.bamboo.core.context.ApplicationContextBean;
import org.panda.tech.data.codegen.ClassBasePackage;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis代码生成器实现
 *
 * @author fangen
 */
public class MybatisCodeGeneratorImpl extends MybatisGeneratorSupport implements MybatisCodeGenerator {

    private String templateLocation = "/templates/mapper.xml.ftl";

    public MybatisCodeGeneratorImpl(ClassBasePackage classBasePackage) {
        super(classBasePackage);
    }

    @Override
    public void generate(String... tableNames) throws Exception {
        if (ArrayUtils.isEmpty(tableNames)) {
            throw new RequiredParamException();
        }
        String tableName = String.join(",", tableNames);
        generate(tableName, false);
    }

    @Override
    public void generate(String tableName, boolean withService) throws Exception {
        DataSource dataSource = ApplicationContextBean.getBean(DataSource.class);
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        // 配置数据库连接参数
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setUrl(metaData.getURL());
//        dataSourceConfig.setDriverName(metaData.getDriverName());
//        dataSourceConfig.setUsername(metaData.getUserName());
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/bamboo_admin_v2");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root123456");
        generateCode(tableName, withService, dataSourceConfig);
    }

    private void generateCode(String tableName, boolean withService, DataSourceConfig dataSourceConfig) throws Exception {
        AutoGenerator generator = new AutoGenerator();
        generator.setDataSource(dataSourceConfig);

        // 配置文件生成路径参数
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("org.panda.business.official");
        packageConfig.setEntity("common.entity");
        packageConfig.setMapper("repository");
        if (withService) {
            packageConfig.setService("service");
            packageConfig.setServiceImpl("service.impl");
        }
        generator.setPackageInfo(packageConfig);

        String projectPath = System.getProperty("user.dir");
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg =  new InjectionConfig() {
            @Override
            public void initMap() {
                // do nothing
            }
        };
        // 调整xml生成目录
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(this.templateLocation) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        generator.setCfg(cfg);

        // 自定义配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//		templateConfig.setEntity("templates/entity2.java");
        if (!withService) {
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
        }
        templateConfig.setController(null);
        templateConfig.setXml(null);
        generator.setTemplate(templateConfig);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("bamboo-code-generator");
        gc.setSwagger2(true); // 是否生成Swagger2注解
        gc.setFileOverride(true); // 是否覆盖同名文件，默认是false
        gc.setOpen(false); // 生成后打开文件夹
        generator.setGlobalConfig(gc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.mybatisplus.extension.activerecord.Model");
        strategy.setEntityLombokModel(true);
        // 开启生成实体时生成字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
//        strategy.setRestControllerStyle(true);
//        strategy.setControllerMappingHyphenStyle(false);
        strategy.setInclude(tableName);
        // 生成的类名去掉t_前缀
        strategy.setTablePrefix("t_");
        generator.setStrategy(strategy);
//        generator.setTemplateEngine(new VelocityTemplateEngine());
		generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }


}
