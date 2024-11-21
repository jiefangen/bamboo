package org.panda.service.doc.model.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExcelDataEnum {
    /**
     * 额度数据模型
     */
    QUOTA_DATA(QuotaExcelData.class, "excelQuota");

    // 设定Excel解析数据模型
    private final Class<?> clazz;
    // 解析模型标签
    private final String tags;
}
