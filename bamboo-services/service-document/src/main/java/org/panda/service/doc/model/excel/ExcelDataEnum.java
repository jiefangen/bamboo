package org.panda.service.doc.model.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExcelDataEnum {
    // 额度数据模型
    QUOTA_DATA(QuotaExcelData.class, "excelQuota"),
    // Excel填充样例模版
    SAMPLE_FILL(SampleExcelFill.class, "excelSampleFill"),
    ;

    /**
     * 设定Excel解析数据模型
     */
    private final Class<?> clazz;
    /**
     * 解析模型标签
     */
    private final String tags;

    /**
     * 根据 tags 获取对应的枚举项
     *
     * @param tags 标签名
     * @return 枚举项
     */
    public static ExcelDataEnum getExelEnumByTags(String tags) {
        for (ExcelDataEnum enumValue : ExcelDataEnum.values()) {
            if (enumValue.getTags().equals(tags)) {
                return enumValue;
            }
        }
        return null;
    }
}
