package org.panda.doc.model.domain;

import lombok.Data;

/**
 * Excel文档模型
 *
 * @author fangen
 **/
@Data
public class ExcelModel extends DocModel{

    private String sheetName;

}
