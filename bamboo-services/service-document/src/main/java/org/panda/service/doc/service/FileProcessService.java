package org.panda.service.doc.service;

import org.panda.service.doc.model.entity.DocFile;
import org.panda.service.doc.model.param.DocFileParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文档文件处理服务
 *
 * @author fangen
 **/
public interface FileProcessService {

    Object importFle(DocFile docFile, InputStream inputStream, boolean md5Verify);

    void fileExport(DocFile docFile, HttpServletResponse response) throws IOException;

    Object excelReadBySheet(InputStream inputStream, DocFileParam docFileParam, String sheetName);
}
