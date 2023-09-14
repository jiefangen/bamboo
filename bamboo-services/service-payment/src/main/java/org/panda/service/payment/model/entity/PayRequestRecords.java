package org.panda.service.payment.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.panda.tech.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pay_request_records")
public class PayRequestRecords extends BaseEntity {

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
     * 请求方式
     */
    @Column(name = "request_mode")
    private String requestMode;

    /**
     * 付款参数集，可组装付款所需参数
     */
    @Column(name = "pay_params")
    private String payParams;

    /**
     * 付款资源链接
     */
    @Column(name = "pay_url")
    private String payUrl;

    /**
     * 支付请求数据
     */
    @Column(name = "request_data")
    private String requestData;

    /**
     * 支付响应数据
     */
    @Column(name = "response_data")
    private String responseData;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

}