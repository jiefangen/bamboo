package org.panda.doc.service;

import org.panda.doc.common.DocConstant;
import org.panda.doc.core.DocFactory;
import org.panda.doc.core.excel.Excel;
import org.springframework.stereotype.Service;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
@Service
public class ExcelService {

    private DocFactory docFactory = new DocFactory();

    public String dataRead() {
        Excel excel = (Excel) docFactory.getDocument(DocConstant.EXCEL);
        excel.read();
        return "";
    }

}
