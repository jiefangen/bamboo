package org.panda.service.doc.service;

import org.panda.service.doc.model.entity.DocFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文档文件处理服务
 *
 * @author fangen
 **/
public interface FileProcessService {

    Object importFle(DocFile docFile, InputStream inputStream);

    void fileExport(DocFile docFile, HttpServletResponse response) throws IOException;
}
