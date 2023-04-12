package org.panda.service.doc.core.domain;

import lombok.Data;
import org.panda.bamboo.common.model.DomainModel;

/**
 * 文档领域模型
 *
 * @author fangen
 **/
@Data
public class DocModel implements DomainModel {

    private String filename;

    private Long size;

}
