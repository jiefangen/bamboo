package org.panda.service.doc.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.panda.data.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class DocFile extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "file_type")
    private String fileType;

}
