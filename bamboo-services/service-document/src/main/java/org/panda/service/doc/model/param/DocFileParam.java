package org.panda.service.doc.model.param;

import lombok.Data;
import org.panda.service.doc.core.domain.document.DocModel;

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
    /**
     * 文件标签
     */
    private String tags;
    /**
     * 文件二进制
     */
    private byte[] fileBytes;
    /**
     * 业务属性
     */
    private String bizAttributes;
}
