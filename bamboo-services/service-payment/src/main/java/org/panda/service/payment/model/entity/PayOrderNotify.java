package org.panda.service.payment.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pay_order_notify")
public class PayOrderNotify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 通知渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 渠道流水号
     */
    @Column(name = "channel_flow_no")
    private String channelFlowNo;

    /**
     * 交易金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 付款资源链接
     */
    @Column(name = "response_status")
    private Integer responseStatus;

    /**
     * 回调通知数据
     */
    @Column(name = "notify_data")
    private String notifyData;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

}