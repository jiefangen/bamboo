package org.panda.service.doc.core.domain.factory.excel.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Excel基础数据模型
 **/
@Getter
@Setter
@EqualsAndHashCode
@ExcelIgnoreUnannotated // 忽略没有注解的字段
public class ExcelBasicData {
    @ExcelProperty(index = 0)
    private String idx0;
    @ExcelProperty(index = 1)
    private String idx1;
    @ExcelProperty(index = 2)
    private String idx2;
    @ExcelProperty(index = 3)
    private String idx3;
    @ExcelProperty(index = 4)
    private String idx4;
    @ExcelProperty(index = 5)
    private String idx5;
    @ExcelProperty(index = 6)
    private String idx6;
    @ExcelProperty(index = 7)
    private String idx7;
    @ExcelProperty(index = 8)
    private String idx8;
    @ExcelProperty(index = 9)
    private String idx9;
}
