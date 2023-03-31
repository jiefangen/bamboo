package org.panda.doc.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class DocFile {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "file_type")
    private String fileType;

}
