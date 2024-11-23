package org.panda.service.doc.service;

import java.util.Map;

/**
 * 文件数据存储服务
 *
 * @author fangen
 **/
public interface DocFileStorageService {
    /**
     * EXCEL数据保存
     *
     * @param docId 文档文件ID
     * @param contentMap 数据集
     */
    void saveExcelDataAsync(Long docId, Map<String, Object> contentMap);

    /**
     * 文件资源保存
     *
     * @param docFileId 文档文件ID
     * @param fileBytes 二进制文件
     */
    void saveFileAsync(Long docFileId, byte[] fileBytes);
}
