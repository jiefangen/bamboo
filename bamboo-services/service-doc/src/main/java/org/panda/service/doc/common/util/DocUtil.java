package org.panda.service.doc.common.util;

import org.apache.commons.io.FilenameUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.constant.file.FileExtensions;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.io.IOUtil;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 文档处理工具类
 *
 * @author fangen
 */
public class DocUtil {

    private DocUtil() {
    }

    private static final Properties MESSAGE = new Properties();
    private static final Properties ERROR = new Properties();

    static {
        Resource messageResource = IOUtil.findI18nResource("classpath:message/document-message",
                Locale.getDefault(), FileExtensions.PROPERTIES);
        Resource errorResource = IOUtil.findI18nResource("classpath:message/document-error",
                Locale.getDefault(), FileExtensions.PROPERTIES);
        if (messageResource != null && errorResource != null) {
            try {
                InputStream messageIn = messageResource.getInputStream();
                MESSAGE.load(messageIn);
                messageIn.close();

                InputStream errorIn = errorResource.getInputStream();
                ERROR.load(errorIn);
                errorIn.close();
            } catch (IOException e) {
                LogUtil.error(DocUtil.class, e);
            }
        }
    }

    public static String getMessage(String key) {
        return MESSAGE.getProperty(key);
    }

    public static String getError(String key) {
        return ERROR.getProperty(key);
    }

    public static String standardizeExtension(String extension) {
        if (extension.startsWith(Strings.DOT)) {
            extension = extension.substring(1);
        }
        return extension.toLowerCase();
    }

    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
