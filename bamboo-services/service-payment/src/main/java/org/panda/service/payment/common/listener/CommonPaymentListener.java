package org.panda.service.payment.common.listener;

import org.panda.bamboo.common.annotation.helper.EnumValueHelper;
import org.panda.service.payment.core.domain.gateway.PaymentListener;
import org.panda.service.payment.core.domain.model.PaymentChannel;
import org.panda.service.payment.model.entity.PayOrder;
import org.panda.service.payment.repository.PayOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 通用支付回调监听器实现
 *
 * @author fangen
 **/
@Component
public class CommonPaymentListener implements PaymentListener {

    @Autowired
    protected PayOrderRepo payOrderRepo;

    @Override
    public void onPaid(PaymentChannel channel, String gatewayPaymentNo, String orderNo, BigDecimal amount) {
        // 主订单状态更新
        PayOrder payOrderParam = new PayOrder();
        payOrderParam.setChannel(EnumValueHelper.getValue(channel));
        payOrderParam.setOrderNo(orderNo);
        Optional<PayOrder> orderOptional = payOrderRepo.findOne(Example.of(payOrderParam));
        if (orderOptional.isPresent()) {
            PayOrder payOrder = orderOptional.get();
            payOrder.setChannelFlowNo(gatewayPaymentNo);
            payOrder.setStatus("paid");
            payOrder.setUpdateTime(LocalDateTime.now());
            payOrderRepo.saveAndFlush(payOrder);
        }
        // TODO 结算业务逻辑
    }
}
