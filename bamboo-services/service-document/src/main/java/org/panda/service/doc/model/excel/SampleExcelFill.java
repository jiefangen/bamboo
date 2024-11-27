package org.panda.service.doc.model.excel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Excel填充样例
 **/
@Getter
@Setter
@EqualsAndHashCode
public class SampleExcelFill {
    private String name;
    private String phone;
    private LocalDateTime date;
}
