package org.panda.doc.core.model;

import lombok.Data;

/**
 * Excel文档数据模型
 *
 * @author fangen
 **/
@Data
public class ExcelModel extends DocModel{

    private String sheetName;

}
