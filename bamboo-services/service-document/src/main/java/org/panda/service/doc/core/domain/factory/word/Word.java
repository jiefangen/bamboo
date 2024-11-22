package org.panda.service.doc.core.domain.factory.word;

import org.panda.service.doc.core.domain.factory.Document;

import java.io.InputStream;
import java.io.OutputStream;

public interface Word extends Document {
    /**
     * Word文档转换Html
     *
     * @param inputStream 文件资源输入流
     * @param outputStream 输出响应流
     * @param extension 文件后缀
     */
    void convertToHtml(InputStream inputStream, OutputStream outputStream, String extension);
}
