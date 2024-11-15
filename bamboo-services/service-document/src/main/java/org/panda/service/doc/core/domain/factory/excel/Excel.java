package org.panda.service.doc.core.domain.factory.excel;

import org.panda.service.doc.core.domain.factory.Document;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Excel extends Document {
    /**
     * 通过EasyExcel读取文件
     *
     * @param inputStream 文件资源输入流
     * @return 数据读取结果
     */
    Map<String, List<Map<Integer, String>>> readByEasyExcel(InputStream inputStream);
}
