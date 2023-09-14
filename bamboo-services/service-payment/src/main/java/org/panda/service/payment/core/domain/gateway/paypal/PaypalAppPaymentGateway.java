package org.panda.service.payment.core.domain.gateway.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.spec.terminal.Program;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;

/**
 * 支付网关：PayPal APP支付
 */
public class PaypalAppPaymentGateway extends PaypalPaymentGateway {

    public PaypalAppPaymentGateway() {
        setTerminals(new Terminal(Program.NATIVE, null, null));
    }

    @Override
    public PaymentRequest prepareRequest(PaymentDefinition definition) {
        return null;
    }

    @Override
    public PaymentResult parseResult(HttpRequestDataProvider notifyDataProvider) {
        APIContext apiContext = new APIContext(getClientId(), getClientSecret(), getMode());
        String paymentId = notifyDataProvider.getParameter("paymentId");
        try {
            return toPaymentResult(Payment.get(apiContext, paymentId));
        } catch (PayPalRESTException e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

}
