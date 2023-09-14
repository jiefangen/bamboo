package org.panda.service.payment.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pay_order_settlement")
public class PayOrderSettlement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 结算单号
     */
    @Column(name = "settle_no")
    private String settleNo;

    /**
     * 结算类型：auto-自动回调结算；manual-人工结算
     */
    @Column(name = "settle_type")
    private String settleType;

    /**
     * 支付渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 渠道流水号
     */
    @Column(name = "channel_flow_no")
    private String channelFlowNo;

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