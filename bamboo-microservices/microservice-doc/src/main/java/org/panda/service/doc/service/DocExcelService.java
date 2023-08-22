package org.panda.service.doc.service;

import org.panda.service.doc.core.domain.model.ExcelModel;
import org.panda.service.doc.model.entity.DocFile;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
public interface DocExcelService {

    Map<String, Object> uploadExcel(InputStream inputStream, DocFile docFile);

    void excelExport(ExcelModel excelModel, ServletOutputStream outputStream);
}
