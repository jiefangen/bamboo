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
@Table(name = "pay_order")
public class PayOrder extends BaseEntity {

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
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 交易金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 交易币种
     */
    @Column(name = "currency")
    private String currency;

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
     * 订单状态：1-pending；2-processing；3-paid；4-failed；5-settled；6-refunded
     */
    @Column(name = "status")
    private String status;

    /**
     * 支付终端
     */
    @Column(name = "terminal")
    private String terminal;

    /**
     * 支付者IP
     */
    @Column(name = "payer_ip")
    private String payerIp;

    /**
     * 支付目标
     */
    @Column(name = "target")
    private String target;

    /**
     * 订单完成ID：结算；退款；
     */
    @Column(name = "completion_id")
    private Long completionId;

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