package org.panda.service.doc.core.domain.factory.excel.easyexcel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Excel基础数据模型
 **/
@Getter
@Setter
@EqualsAndHashCode
public class ExcelBasicData {
    private String string;
    private Date date;
    private Double doubleData;
}
