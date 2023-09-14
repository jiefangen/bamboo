package org.panda.service.doc.core.domain.factory;

import org.panda.service.doc.core.domain.document.DocModel;

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
     * @param docModel 文档源数据
     */
    void create(OutputStream outputStream, DocModel docModel);

    /**
     * 文档转换
     *
     * @param inputStream 文件资源输入流
     * @param outputStream 输出响应流
     * @param extension 文件后缀
     */
    void convert(InputStream inputStream, OutputStream outputStream, String extension);
}
