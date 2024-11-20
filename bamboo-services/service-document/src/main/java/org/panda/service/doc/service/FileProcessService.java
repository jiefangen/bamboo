package org.panda.service.doc.service;

import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.model.param.ExcelDocFileParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文档文件处理服务
 *
 * @author fangen
 **/
public interface FileProcessService {

    Object importFile(DocFileParam docFileParam, InputStream inputStream, boolean docVerify);

    void fileExport(Long fileId, HttpServletResponse response) throws IOException;

    <T> Object excelReadBySheet(InputStream inputStream, ExcelDocFileParam docFileParam, Class<T> dataClass, boolean docVerify);

    <T> void excelExport(HttpServletResponse response, Long fileId, Class<T> dataClass, String tags) throws IOException;
}
