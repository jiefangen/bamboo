package org.panda.ms.doc.service;

import org.panda.ms.doc.model.entity.DocFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文档文件服务
 *
 * @author fangen
 **/
public interface DocFileService {

    Object importFle(DocFile docFile, InputStream inputStream);

    void fileExport(DocFile docFile, HttpServletResponse response) throws IOException;
}
