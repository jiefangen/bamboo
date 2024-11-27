package org.panda.service.doc.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.service.doc.model.param.DocFileParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文档文件工具类
 *
 * @author fangen
 * @since 2024/11/19
 **/
public class DocFileUtils {

    public static String standardizeExtension(String extension) {
        if (extension.startsWith(Strings.DOT)) {
            extension = extension.substring(1);
        }
        return extension.toLowerCase();
    }

    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static String appendFilename(String originalName, String suffix) {
        // 检查原始文件名是否包含扩展名
        int lastDotIndex = originalName.lastIndexOf(Strings.DOT);
        if (lastDotIndex == -1) {
            // 如果没有扩展名，直接添加后缀
            return originalName + suffix;
        }
        // 分割文件名和扩展名
        String baseName = originalName.substring(0, lastDotIndex);
        String extension = originalName.substring(lastDotIndex);
        return baseName + suffix + extension;
    }

    public static void transformDocFile(MultipartFile file, DocFileParam docFileParam) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension = DocFileUtils.getExtension(filename);
        docFileParam.setFilename(filename);
        docFileParam.setFileType(fileExtension);
        docFileParam.setFileSize(file.getSize());
        docFileParam.setFileBytes(file.getBytes());
    }
}
