package org.panda.service.doc.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.model.entity.DocExcelData;
import org.panda.service.doc.repository.DocExcelDataRepo;
import org.panda.service.doc.service.DocExcelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocExcelDataServiceImpl implements DocExcelDataService {

    @Autowired
    private DocExcelDataRepo docExcelDataRepo;

    @Async
    @Override
    public void saveExcelDataAsync(Long docId, Map<String, List<Map<Integer, String>>> contentMap) {
        if (!contentMap.isEmpty()) {
            for (Map.Entry<String, List<Map<Integer, String>>> entry : contentMap.entrySet()) {
                String sheetName = entry.getKey();
                List<Map<Integer, String>> valueList = entry.getValue();
                if (CollectionUtils.isNotEmpty(valueList)) {
                    List<DocExcelData> excelDataList = new ArrayList<>();
                    for (int i = 0; i < valueList.size(); i++) {
                        Map<Integer, String> map = valueList.get(i);
                        if (!map.isEmpty()) {
                            for (Map.Entry<Integer, String> valueEntry : map.entrySet()) {
                                DocExcelData excelData = new DocExcelData();
                                excelData.setDocId(docId);
                                excelData.setSheetName(sheetName);
                                excelData.setRowIndex(i);
                                excelData.setColumnIndex(valueEntry.getKey());
                                excelData.setCellValue(valueEntry.getValue());
                                excelDataList.add(excelData);
                            }
                        }
                    }
                    docExcelDataRepo.saveAll(excelDataList);
                }
            }
        }
        LogUtil.info(getClass(), "Storing Excel data completed, docId is {}", docId);
    }
}
