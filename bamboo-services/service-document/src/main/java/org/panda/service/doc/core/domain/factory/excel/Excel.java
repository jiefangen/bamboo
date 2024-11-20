package org.panda.service.doc.core.domain.factory.excel;

import org.panda.service.doc.core.domain.factory.Document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface Excel extends Document {
    /**
     * 通用EasyExcel读取文件
     *
     * @param inputStream 文件资源输入流
     * @return 数据读取结果
     */
    Map<String, List<Map<Integer, String>>> readByEasyExcel(InputStream inputStream);

    /**
     * 指定映射class读取
     *
     * @param inputStream 文件流
     * @param sheetName Sheet名称
     * @param dataClass 指定映射类
     * @return 读取结果
     * @param <T> 泛型
     */
    <T> Map<String, List<T>> readByEasyExcel(InputStream inputStream, String sheetName, Class<T> dataClass);

    /**
     * 指定映射class写入
     *
     * @param outputStream 输出流
     * @param dataMap 数据集
     * @param dataClass 指定映射类
     * @param <T> 泛型
     */
    <T> void writeByEasyExcel(OutputStream outputStream, Map<String, List<T>> dataMap, Class<T> dataClass);
}
