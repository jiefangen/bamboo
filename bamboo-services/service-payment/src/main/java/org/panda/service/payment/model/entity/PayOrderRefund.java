package org.panda.service.payment.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pay_order_refund")
public class PayOrderRefund extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退款单号
     */
    @Column(name = "refund_no")
    private String refundNo;

    /**
     * 退款渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 渠道流水号
     */
    @Column(name = "channel_flow_no")
    private String channelFlowNo;

    /**
     * 退款状态：1-pending；2-processing；3-completed；4-failed；
     */
    @Column(name = "status")
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 人工操作ID
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

}