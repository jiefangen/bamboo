package org.panda.service.doc.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "doc_file_storage")
public class DocFileStorage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文档文件ID
     */
    @Column(name = "file_id")
    private Long fileId;

    /**
     * 二进制文件
     */
    @Lob
    @Column(name = "file_binary", columnDefinition = "LONGBLOB")
    private byte[] fileBinary;

    /**
     * 0-异常/损坏；1-正常；2-删除；
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 文件存储位置
     */
    @Column(name = "storage_location")
    private String storageLocation;

}