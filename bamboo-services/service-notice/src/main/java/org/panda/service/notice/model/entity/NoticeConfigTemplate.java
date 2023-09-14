package org.panda.service.notice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "notice_config_template")
public class NoticeConfigTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模版名称
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * 模版内容标题
     */
    @Column(name = "template_content_title")
    private String templateContentTitle;

    /**
     * 模版内容
     */
    @Column(name = "template_content")
    private String templateContent;

    /**
     * 通知方式
     */
    @Column(name = "notice_mode")
    private String noticeMode;

    /**
     * 通知内容提供者类型
     */
    @Column(name = "notice_provider_type")
    private String noticeProviderType;

    /**
     * 通知发送器类型
     */
    @Column(name = "notice_sender_type")
    private String noticeSenderType;

    /**
     * 类别
     */
    @Column(name = "category")
    private String category;

    /**
     * 是否激活
     */
    @Column(name = "is_active")
    private Boolean isActive;

}