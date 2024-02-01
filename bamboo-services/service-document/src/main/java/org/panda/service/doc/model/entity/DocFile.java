package org.panda.service.doc.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "doc_file")
public class DocFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名称
     */
    @Column(name = "filename")
    private String filename;

    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文件md5值
     */
    @Column(name = "file_md5")
    private String fileMd5;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 文本内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 可访问性
     */
    @Column(name = "accessibility")
    private Boolean accessibility;

    /**
     * 标签
     */
    @Column(name = "tags")
    private String tags;

    /**
     * 类别
     */
    @Column(name = "category")
    private String category;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}