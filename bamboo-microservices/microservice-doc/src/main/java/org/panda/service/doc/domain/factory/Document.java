package org.panda.service.doc.domain.factory;

import org.panda.service.doc.domain.model.DocModel;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;

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
     * @param docModel 文档源数据
     * @param outputStream 输出响应文件流
     */
    void create(DocModel docModel, ServletOutputStream outputStream);

    /**
     * 文档预览
     *
     * @param outputStream 输出响应文件流
     */
    void preview(ServletOutputStream outputStream);
}
