package org.panda.service.doc.common.utils;

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
public class DocumentUtils {

    private DocumentUtils() {
    }

    private static final Properties MESSAGE = new Properties();
    private static final Properties ERROR = new Properties();

    static {
        Resource messageResource = IOUtil.findI18nResource("classpath:message/info",
                Locale.getDefault(), FileExtensions.PROPERTIES);
        Resource errorResource = IOUtil.findI18nResource("classpath:message/error",
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
                LogUtil.error(DocumentUtils.class, e);
            }
        }
    }

    public static String getMessage(String key) {
        return MESSAGE.getProperty(key);
    }

    public static String getError(String key) {
        return ERROR.getProperty(key);
    }
}
