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

    Object importFile(DocFileParam docFileParam, InputStream inputStream, boolean md5Verify);

    void fileExport(DocFile docFile, HttpServletResponse response) throws IOException;

    <T> Object excelReadBySheet(InputStream inputStream, DocFileParam docFileParam, Class<T> dataClass);
}
