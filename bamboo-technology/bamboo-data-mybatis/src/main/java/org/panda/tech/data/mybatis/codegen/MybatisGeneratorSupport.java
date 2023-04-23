package org.panda.tech.data.mybatis.codegen;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.panda.bamboo.common.constant.Strings;
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
            generator.setDataSource(dataSourceConfig);

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
            generator.setPackageInfo(packageConfig);

            String projectPath = System.getProperty("user.dir");

            // 调整xml生成目录位置
            InjectionConfig injectionConfig =  new InjectionConfig() {
                @Override
                public void initMap() {
                }
            };
            List<FileOutConfig> focList = new ArrayList<>();
            focList.add(new FileOutConfig(templateLocation) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
                }
            });
            injectionConfig.setFileOutConfigList(focList);
            generator.setCfg(injectionConfig);

            // 自定义配置模板，指定自定义模板路径
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(null);
            templateConfig.setController(null);
            generator.setTemplate(templateConfig);

            // 全局配置
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setOutputDir(projectPath + "/src/main/java");
            globalConfig.setAuthor("bamboo-code-generator");
            globalConfig.setSwagger2(true); // 是否生成Swagger2注解
            globalConfig.setFileOverride(false); // 是否覆盖同名文件，默认是false
            globalConfig.setOpen(false); // 生成后打开文件夹
            generator.setGlobalConfig(globalConfig);

            // 生成策略配置
            StrategyConfig strategyConfig = new StrategyConfig();
            strategyConfig.setNaming(NamingStrategy.underline_to_camel);
            strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
            strategyConfig.setEntityLombokModel(true);
            strategyConfig.setEntityTableFieldAnnotationEnable(true); // 开启生成实体时生成字段注解
            strategyConfig.setInclude(tableName);
            strategyConfig.setTablePrefix(tablePrefix);

            generator.setStrategy(strategyConfig);
            generator.setTemplateEngine(new FreemarkerTemplateEngine());
            generator.execute();
        }
    }

}
