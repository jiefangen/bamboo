package org.panda.service.doc.core.domain.factory.excel;

import lombok.Data;
import org.panda.service.doc.core.domain.document.DocModel;

/**
 * Excel文档模型
 *
 * @author fangen
 **/
@Data
public class ExcelModel extends DocModel {
    /**
     * Sheet名称
     */
    private String sheetName;
}
