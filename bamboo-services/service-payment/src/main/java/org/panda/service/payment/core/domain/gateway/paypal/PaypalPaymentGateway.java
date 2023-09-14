package org.panda.service.payment.core.domain.gateway.paypal;

import com.paypal.api.payments.*;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.service.payment.core.domain.gateway.AbstractPaymentGateway;
import org.panda.service.payment.core.domain.model.PaymentChannel;
import org.panda.service.payment.core.domain.model.PaymentResult;

import java.math.BigDecimal;

/**
 * 支付网关：抽象PayPal支付
 */
public abstract class PaypalPaymentGateway extends AbstractPaymentGateway {

    private String clientId;
    private String clientSecret;
    private String mode;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    protected String getClientId() {
        return this.clientId;
    }

    protected String getClientSecret() {
        return this.clientSecret;
    }

    protected String getMode() {
        return this.mode;
    }

    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.PAYPAL;
    }

    protected PaymentResult toPaymentResult(Payment executeResult) {
        this.logger.info(executeResult.toJSON());
        if ("approved".equals(executeResult.getState())) {
            Transaction transaction = executeResult.getTransactions().get(0);
            RelatedResources relatedResources = transaction.getRelatedResources().get(0);
            Sale sale = relatedResources.getSale();

            String gatewayPaymentNo = sale.getId();
            BigDecimal amount = MathUtil.parseDecimal(sale.getAmount().getTotal());
            Item item = transaction.getItemList().getItems().get(0);
            String orderNo = item.getSku();

            return new PaymentResult(orderNo, gatewayPaymentNo, amount, Commons.RESULT_SUCCESS);
        }
        return null;
    }
}
