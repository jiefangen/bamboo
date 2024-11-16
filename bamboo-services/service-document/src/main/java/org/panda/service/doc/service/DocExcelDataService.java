package org.panda.service.doc.service;

import java.util.Map;

/**
 * excel文件数据服务
 *
 * @author fangen
 **/
public interface DocExcelDataService {

    void saveExcelDataAsync(Long docId, Map<String, Object> contentMap);
}
