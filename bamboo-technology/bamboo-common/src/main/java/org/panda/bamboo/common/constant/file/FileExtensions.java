package org.panda.bamboo.common.constant.file;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 文件扩展名常量集类
 */
public class FileExtensions {

    private FileExtensions() {
    }

    // 文本文档
    public static final String TXT = "txt";
    public static final String MARKDOWN = "md";

    // 图片
    public static final String BMP = "bmp";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
    public static final String PNG = "png";
    public static final String SVG = "svg";
    public static final String GIF = "gif";
    public static final String[] IMAGES_NORMAL = { BMP, JPG, JPEG, PNG, SVG };
    public static final String[] IMAGES_ALL = { BMP, JPG, JPEG, PNG, SVG, GIF };

    // PDF文档
    public static final String PDF = "pdf";

    // Office文档
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static final String PPT = "ppt";
    public static final String PPTX = "pptx";

    /**
     * 可读取文本的二进制文件扩展名集
     */
    public static final String[] TEXT_READABLE_BINARY = { PDF, XLS, XLSX, DOC, DOCX, PPT, PPTX };

    // 配置文件
    public static final String PROPERTIES = "properties";
    public static final String YAML = "yaml";
    public static final String YML = "yml";

    // 网页文件
    public static final String HTML = "html";

    // 代码文件
    public static final String[] CODES = { "java", "jsp", "js", "jsx", "ts", "sql",
            "xml", "html", "htm", "xhtml", "rss", "atom", "xjb", "xsd", "xsl", "plist", "wxml",
            "vue", "ftl", "vm", "asp", "aspx",
            "properties", "yaml", "yml", "json",
            "css", "wxss", "less", "scss",
            "c", "h", "cc", "cpp", "cxx", "hh", "hpp", "hxx", "cs",
            "bash", "sh", "zsh", "py", "shell",
            "php", "swift", "kt", "go", "lua", "gradle", "groovy", "gitignore" };

    public static boolean isCode(String extension) {
        return ArrayUtils.contains(CODES, extension);
    }

}
