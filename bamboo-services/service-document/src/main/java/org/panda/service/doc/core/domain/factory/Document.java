package org.panda.service.doc.core.domain.factory;

import java.io.InputStream;
import java.io.OutputStream;

public interface Document {

    /**
     * 文档数据读取归纳
     *
     * @param inputStream 文件资源输入流
     * @param extension 文件后缀
     * @return 数据读取结果
     */
    Object read(InputStream inputStream, String extension);

    /**
     * 文档创建生成
     *
     * @param outputStream 输出响应文件流
     * @param content 文档源数据内容
     */
    void create(OutputStream outputStream, String content);
}
