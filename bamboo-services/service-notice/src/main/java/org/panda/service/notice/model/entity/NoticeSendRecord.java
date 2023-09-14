package org.panda.service.notice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "notice_send_record")
public class NoticeSendRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 通知方式
     */
    @Column(name = "notice_mode")
    private String noticeMode;

    /**
     * 通知类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 通知发送目标
     */
    @Column(name = "notice_targets")
    private String noticeTargets;

    /**
     * 通知状态：1-成功；0-失败；
     */
    @Column(name = "notice_status")
    private Integer noticeStatus;

    /**
     * 通知结果
     */
    @Column(name = "notice_result")
    private String noticeResult;

    /**
     * 标签
     */
    @Column(name = "tags")
    private String tags;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private LocalDateTime sendTime;

    /**
     * 接收时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

}