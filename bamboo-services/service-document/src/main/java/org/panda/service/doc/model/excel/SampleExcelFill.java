package org.panda.service.doc.model.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Excel填充样例
 **/
@Getter
@Setter
@EqualsAndHashCode
public class SampleExcelFill {
    private String name;
    private String phone;
    private int age;
}
