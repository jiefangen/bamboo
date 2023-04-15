package org.panda.service.doc.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "doc_file")
public class DocFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content")
    private String content;

    @Column(name = "accessibility")
    private Byte accessibility;

    @Column(name = "tags")
    private String tags;

    @Column(name = "category")
    private String category;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

}