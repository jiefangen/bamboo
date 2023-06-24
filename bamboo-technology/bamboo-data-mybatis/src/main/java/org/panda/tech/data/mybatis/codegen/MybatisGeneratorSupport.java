package org.panda.tech.data.mybatis.codegen;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.tech.data.codegen.ClassBasePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis生成器支持
 *
 * @author fangen
 */
public abstract class MybatisGeneratorSupport {

    private ClassBasePackage classBasePackage;

    public MybatisGeneratorSupport(ClassBasePackage classBasePackage) {
        this.classBasePackage = classBasePackage;
    }

    protected void generate(String templateLocation, DataSourceConfig dataSourceConfig, String tableName,
                            boolean withService, String tablePrefix) {
        if (templateLocation != null) {
            AutoGenerator generator = new AutoGenerator();
            generator.setDataSource(getDataSourceConfig(dataSourceConfig));
            generator.setPackageInfo(getPackageConfig(withService));

            String projectPath = System.getProperty("user.dir");
            generator.setCfg(getInjectionConfig(templateLocation, projectPath));

            generator.setTemplate(getTemplateConfig(withService));
            generator.setGlobalConfig(getGlobalConfig(projectPath));

            generator.setStrategy(getStrategyConfig(tableName, tablePrefix));
            generator.setTemplateEngine(new FreemarkerTemplateEngine());
            generator.execute();
        }
    }

    private DataSourceConfig getDataSourceConfig(DataSourceConfig dataSourceConfig) {
        // tinyint转换成Boolean
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                if (fieldType.toLowerCase().contains("tinyint")) {
                    return DbColumnType.BOOLEAN;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });
        return dataSourceConfig;
    }

    private PackageConfig getPackageConfig(boolean withService) {
        // 配置文件生成路径参数
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(this.classBasePackage.getParentLocation());
        packageConfig.setEntity(this.classBasePackage.getEntityLocation());
        packageConfig.setMapper(this.classBasePackage.getRepositoryLocation());
        if (withService) {
            String servicePackage = this.classBasePackage.getServiceLocation();
            packageConfig.setService(servicePackage);
            packageConfig.setServiceImpl(servicePackage + Strings.DOT + "impl");
        }
        return packageConfig;
    }

    private InjectionConfig getInjectionConfig(String templateLocation, String projectPath) {
        // 调整xml生成目录位置
        InjectionConfig injectionConfig =  new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        String mapperXmlLocation = Strings.EMPTY;
        if (StringUtils.isNotEmpty(classBasePackage.getMapperXmlLocation())) {
            mapperXmlLocation = classBasePackage.getMapperXmlLocation() + Strings.SLASH;
        }
        String finalMapperXmlLocation = mapperXmlLocation;
        focList.add(new FileOutConfig(templateLocation) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + finalMapperXmlLocation + tableInfo.getEntityName()
                        + "Mapper.xml";
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }

    private TemplateConfig getTemplateConfig(boolean withService) {
        // 自定义配置模板，指定自定义模板路径
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        if (!withService) {
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
        }
        return templateConfig;
    }

    private GlobalConfig getGlobalConfig(String projectPath) {
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("bamboo-code-generator");
        globalConfig.setServiceName("%sService");
        globalConfig.setSwagger2(true); // 是否生成Swagger2注解
        globalConfig.setFileOverride(false); // 是否覆盖同名文件，默认是false
        globalConfig.setOpen(false); // 生成后打开文件夹
        return globalConfig;
    }

    private StrategyConfig getStrategyConfig(String tableName, String tablePrefix) {
        // 生成策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true); // 开启生成实体时生成字段注解
        if (tableName.contains(",")) {
            strategyConfig.setInclude(tableName.split("\\,"));
        } else {
            strategyConfig.setInclude(tableName);
        }
        strategyConfig.setTablePrefix(tablePrefix);
        return strategyConfig;
    }

}
