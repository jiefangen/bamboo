package org.panda.bamboo.common.parser;

import freemarker.template.Configuration;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.basic.Times;

/**
 * FreeMarker助手
 */
public class FreeMarkerHelper {

    private FreeMarkerHelper() {
    }

    public static Configuration getDefaultConfiguration() {
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        applyDefault(config);
        return config;
    }

    public static void applyDefault(Configuration config) {
        config.setClassicCompatible(true);
        config.setNumberFormat("0.##");
        config.setTimeFormat(Times.TIME_PATTERN);
        config.setDateFormat(Times.SHORT_DATE_PATTERN);
        config.setDateTimeFormat(Times.LONG_DATE_PATTERN);
        config.clearEncodingMap();
        config.setDefaultEncoding(Strings.ENCODING_UTF8);
    }

}
