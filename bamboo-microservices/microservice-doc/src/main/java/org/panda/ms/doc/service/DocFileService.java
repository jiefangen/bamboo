package org.panda.ms.doc.service;

import org.panda.ms.doc.model.entity.DocFile;
import org.panda.ms.doc.model.param.DocFileQueryParam;
import org.panda.tech.data.model.query.QueryResult;

/**
 * 文档文件数据服务
 *
 * @author fangen
 **/
public interface DocFileService {

    QueryResult<DocFile> getDocFileByPage(DocFileQueryParam queryParam);

    QueryResult<DocFile> getDocument(DocFileQueryParam queryParam);
}
