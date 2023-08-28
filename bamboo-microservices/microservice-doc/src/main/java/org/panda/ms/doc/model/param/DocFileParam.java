package org.panda.ms.doc.model.param;

import lombok.Data;
import org.panda.ms.doc.core.domain.document.DocModel;

/**
 * 文档文件上传参数
 *
 * @author fangen
 **/
@Data
public class DocFileParam extends DocModel {
    /**
     * 文件base64
     */
    private String fileBase64;
}
