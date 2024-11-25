package org.panda.service.doc.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.service.doc.model.entity.DocExcelData;
import org.panda.service.doc.model.entity.DocFileStorage;
import org.panda.service.doc.repository.DocExcelDataRepo;
import org.panda.service.doc.repository.DocFileStorageRepo;
import org.panda.service.doc.service.DocFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocFileStorageServiceImpl implements DocFileStorageService {

    @Autowired
    private DocExcelDataRepo docExcelDataRepo;
    @Autowired
    private DocFileStorageRepo docFileStorageRepo;

    private void buildExcelData(List<DocExcelData> excelDataList, String cellValue, Long docId, String sheetName,
                                int rowIndex, int columnIndex) {
        DocExcelData excelData = new DocExcelData();
        excelData.setDocFileId(docId);
        excelData.setSheetName(sheetName);
        excelData.setRowIndex(rowIndex);
        excelData.setColumnIndex(columnIndex);
        excelData.setCellValue(cellValue);
        excelDataList.add(excelData);
    }

    @Async
    @Override
    public void saveExcelDataAsync(Long docId, Map<String, Object> contentMap) {
        if (!contentMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : contentMap.entrySet()) {
                String sheetName = entry.getKey();
                Object valueObj = entry.getValue();
                if (valueObj instanceof List) { // EasyExcel方式读取解析
                    List<Map<Integer, String>> valueList = (List<Map<Integer, String>>) valueObj;
                    if (CollectionUtils.isNotEmpty(valueList)) {
                        List<DocExcelData> excelDataList = new ArrayList<>();
                        for (int i = 0; i < valueList.size(); i++) {
                            Map<Integer, String> map = valueList.get(i);
                            if (!map.isEmpty()) {
                                for (Map.Entry<Integer, String> valueEntry : map.entrySet()) {
                                    buildExcelData(excelDataList, valueEntry.getValue(), docId, sheetName, i,
                                            valueEntry.getKey());
                                }
                            }
                        }
                        docExcelDataRepo.saveAll(excelDataList);
                    }
                } else { // POI传统方式读取解析
                    String[][] valueArr = (String[][]) valueObj;
                    List<DocExcelData> excelDataList = new ArrayList<>();
                    for (int i = 0; i < valueArr.length; i++) { // 遍历行
                        for (int j = 0; j < valueArr[i].length; j++) {  // 遍历列
                            buildExcelData(excelDataList, valueArr[i][j], docId, sheetName, i, j);
                        }
                    }
                    docExcelDataRepo.saveAll(excelDataList);
                }
            }
        }
        LogUtil.info(getClass(), "Storing Excel data completed, docFileId is {}", docId);
    }

    @Async
    @Override
    public void saveFileAsync(Long docFileId, byte[] fileBytes) {
        DocFileStorage fileStorage = new DocFileStorage();
        if (fileBytes == null || fileBytes.length < 1) {
            fileStorage.setStatus(0);
        } else {
            fileStorage.setStatus(1);
        }
        fileStorage.setDocFileId(docFileId);
        fileStorage.setFileBinary(fileBytes);
        fileStorage.setStorageLocation("db");
        docFileStorageRepo.save(fileStorage);
        LogUtil.info(getClass(), "Storing File completed, docFileId is {}", docFileId);
    }
}
