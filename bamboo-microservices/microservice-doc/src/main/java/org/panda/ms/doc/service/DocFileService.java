package org.panda.ms.doc.service;

import org.panda.ms.doc.model.entity.DocFile;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;

/**
 * 文档文件服务
 *
 * @author fangen
 **/
public interface DocFileService {

    Object uploadExcel(DocFile docFile, InputStream inputStream);

    void excelExport(DocFile docFile, ServletOutputStream outputStream);

}
