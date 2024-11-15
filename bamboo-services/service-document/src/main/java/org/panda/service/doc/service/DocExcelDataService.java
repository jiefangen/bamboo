package org.panda.service.doc.service;

import java.util.List;
import java.util.Map;

/**
 * excel文件数据服务
 *
 * @author fangen
 **/
public interface DocExcelDataService {

    void saveExcelDataAsync(Long docId, Map<String, List<Map<Integer, String>>> contentMap);
}
