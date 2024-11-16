package org.panda.service.doc.model.excel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 额度Excel数据模型
 **/
@Getter
@Setter
@EqualsAndHashCode
@ExcelIgnoreUnannotated
public class QuotaExcelData {
    @ExcelProperty("到手金额")
    private Long receivedAmount;
    @ExcelProperty("授信金额")
    private Long creditAmount;
    @ExcelProperty("还款金额")
    private Long repaymentAmount;
    @ExcelProperty("等级")
    private Integer level;
    @ExcelProperty("周期")
    private Integer period;
    @ExcelProperty("展示天数")
    private Integer displayDays;
    @ExcelProperty("备注")
    private String remark;
}
