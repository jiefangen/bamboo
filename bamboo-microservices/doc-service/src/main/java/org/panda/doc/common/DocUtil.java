package org.panda.doc.common;

import org.apache.commons.io.FilenameUtils;
import org.panda.bamboo.common.constant.StringsConstant;

/**
 * 文档处理工具类
 *
 * @author fangen
 */
public class DocUtil {

    public static String standardizeExtension(String extension) {
        if (extension.startsWith(StringsConstant.DOT)) {
            extension = extension.substring(1);
        }
        return extension.toLowerCase();
    }

    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
