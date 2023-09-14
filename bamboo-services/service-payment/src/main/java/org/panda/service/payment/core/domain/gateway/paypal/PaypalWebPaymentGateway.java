package org.panda.service.payment.core.domain.gateway.paypal;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.panda.service.payment.core.domain.model.PaymentDefinition;
import org.panda.ms.payment.core.domain.model.*;
import org.panda.service.payment.core.domain.model.PaymentRequest;
import org.panda.service.payment.core.domain.model.PaymentRequestMode;
import org.panda.service.payment.core.domain.model.PaymentResult;
import org.panda.tech.core.spec.terminal.Program;
import org.panda.tech.core.spec.terminal.Terminal;
import org.panda.tech.core.web.mvc.servlet.http.HttpRequestDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付网关：PayPal WEB支付
 */
public class PaypalWebPaymentGateway extends PaypalPaymentGateway {

    public PaypalWebPaymentGateway() {
        setTerminals(new Terminal(Program.WEB, null, null));
    }

    @Override
    public PaymentRequest prepareRequest(PaymentDefinition definition) {
        // 建立金额与币种
        Amount amountDetail = new Amount();
        amountDetail.setCurrency(definition.getCurrency().toString());
        amountDetail.setTotal(definition.getAmount().toString());
        Transaction transaction = new Transaction();
        transaction.setAmount(amountDetail);
        transaction.setInvoiceNumber(definition.getOrderNo());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // 建立支付方式
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setNoteToPayer(definition.getDescription());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(getResultShowUrl());
        redirectUrls.setReturnUrl(getResultConfirmUrl());// 回调路径
        payment.setRedirectUrls(redirectUrls);
        try {
            APIContext apiContext = new APIContext(getClientId(), getClientSecret(), getMode());
            Payment createdPayment = payment.create(apiContext);
            for (Links links : createdPayment.getLinks()) {
                if ("approval_url".equals(links.getRel())) {
                    String url = links.getHref();
                    return new PaymentRequest(url, PaymentRequestMode.GET_LINK, null);
                }
            }
        } catch (PayPalRESTException e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public PaymentResult parseResult(HttpRequestDataProvider notifyDataProvider) {
        Payment payment = new Payment();
        payment.setId(notifyDataProvider.getParameter("paymentId"));
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(notifyDataProvider.getParameter("PayerID"));
        try {
            APIContext apiContext = new APIContext(getClientId(), getClientSecret(), getMode());
            return toPaymentResult(payment.execute(apiContext, paymentExecution));
        } catch (PayPalRESTException e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

}
