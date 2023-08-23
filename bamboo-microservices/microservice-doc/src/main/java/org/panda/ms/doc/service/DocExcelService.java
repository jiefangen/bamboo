package org.panda.ms.doc.service;

import org.panda.ms.doc.model.entity.DocFile;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Excel文档操作服务
 *
 * @author fangen
 **/
public interface DocExcelService {

    Map<String, Object> uploadExcel(DocFile docFile, InputStream inputStream);

    void excelExport(DocFile docFile, ServletOutputStream outputStream);
}
