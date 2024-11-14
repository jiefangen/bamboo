package org.panda.service.doc.common;

/**
 * 文档处理异常错误码集
 */
public class DocExceptionCodes {

    private DocExceptionCodes() {
    }

    /**
     * 无法加载
     */
    public static final String CAN_NOT_LOAD = "error.doc.can_not_load";

    /**
     * 文件类型不支持
     */
    public static final String TYPE_NOT_SUPPORT = "error.doc.type_not_support";
    /**
     * 文件已存在
     */
    public static final String FILE_EXISTS = "error.doc.file_exists";

}
