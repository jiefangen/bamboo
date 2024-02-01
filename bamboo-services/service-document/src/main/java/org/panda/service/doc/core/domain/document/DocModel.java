package org.panda.service.doc.core.domain.document;

import lombok.Data;
import org.panda.bamboo.common.model.DomainModel;

/**
 * 文档领域模型
 *
 * @author fangen
 **/
@Data
public class DocModel implements DomainModel {
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件内容
     */
    private String content;
}
