package org.panda.service.doc.service;

import org.panda.service.doc.model.param.DocFileParam;
import org.panda.service.doc.model.param.ExcelDocFileParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文档文件处理服务
 *
 * @author fangen
 **/
public interface FileProcessService {

    Object importFile(DocFileParam docFileParam, InputStream inputStream, boolean docVerify);

    void fileExport(Long fileId, HttpServletResponse response) throws IOException;

    void createFile(Long fileId, HttpServletResponse response) throws IOException;

    <T> Object excelReadBySheet(InputStream inputStream, ExcelDocFileParam docFileParam, boolean docVerify);

    <T> void excelExport(HttpServletResponse response, Long fileId) throws IOException;

    <T> void exportFill(HttpServletResponse response, Long fileId, List<T> dataList, Map<String, Object> map) throws IOException;
}
