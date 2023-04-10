package org.panda.bamboo.common.parser;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.panda.bamboo.common.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * 通过FreeMarker实现的模板解析器
 */
public class FreeMarkerTemplateParser implements TemplateParser {

    private Configuration config = FreeMarkerHelper.getDefaultConfiguration();

    public void setNumberFormat(String numberFormat) {
        this.config.setNumberFormat(numberFormat);
    }

    public void setTimeFormat(String timeFormat) {
        this.config.setTimeFormat(timeFormat);
    }

    public void setDateFormat(String dateFormat) {
        this.config.setDateFormat(dateFormat);
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.config.setDateTimeFormat(dateTimeFormat);
    }

    public void setDefaultEncoding(String encoding) {
        this.config.setDefaultEncoding(encoding);
    }

    @Override
    public String parse(String templateContent, Map<String, Object> params, Locale locale) {
        Configuration config = (Configuration) this.config.clone();
        if (locale != null) {
            config.setLocale(locale);
        }
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", templateContent);
        config.setTemplateLoader(stringLoader);
        try {
            Template t = config.getTemplate("template");
            StringWriter out = new StringWriter();
            t.process(params, out);
            return out.toString();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        } catch (TemplateException e) {
            LogUtil.error(getClass(), e);
            // 模板格式错误，返回模板本身
            return templateContent;
        }
        return null;
    }

    @Override
    public String parse(File templateFile, Map<String, Object> params, Locale locale)
            throws IOException {
        Configuration config = (Configuration) this.config.clone();
        if (locale != null) {
            config.setLocale(locale);
        }
        try {
            config.setDirectoryForTemplateLoading(templateFile.getParentFile());
            Template t = config.getTemplate(templateFile.getName());
            StringWriter out = new StringWriter();
            t.process(params, out);
            return out.toString();
        } catch (TemplateException e) {
            LogUtil.error(getClass(), e);
            // 模板格式错误，返回模板内容
            return IOUtils.toString(new FileInputStream(templateFile), StandardCharsets.UTF_8);
        }
    }
}
