package org.panda.ms.doc.core.domain.model;

import lombok.Data;

/**
 * Excel文档模型
 *
 * @author fangen
 **/
@Data
public class ExcelModel extends DocModel {

    private String sheetName;

}
