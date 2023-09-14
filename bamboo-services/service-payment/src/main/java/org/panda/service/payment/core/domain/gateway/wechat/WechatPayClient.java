package org.panda.service.payment.core.domain.gateway.wechat;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

/**
 * 微信支付客户端
 */
public class WechatPayClient {

    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 应用id
     */
    private String appId;
    /**
     * API证书序列号
     */
    private String certSerialNo;
    /**
     * API证书私钥（在官方文档中有时称作“商户私钥”）
     */
    private PrivateKey certPrivateKey;
    /**
     * APIv3密钥
     */
    private String apiV3Key;

    private HttpClient client;

    public WechatPayClient(String merchantId, String appId, String certSerialNo, Resource certPrivateKeyResource,
            String apiV3Key) throws Exception {
        this.merchantId = merchantId;
        this.appId = appId;
        this.certSerialNo = certSerialNo;
        this.certPrivateKey = PemUtil.loadPrivateKey(certPrivateKeyResource.getInputStream());
        this.apiV3Key = apiV3Key;
        this.client = buildClient();
    }

    private HttpClient buildClient() throws Exception {
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(this.certSerialNo, this.certPrivateKey);
        WechatPay2Credentials credentials = new WechatPay2Credentials(this.merchantId, privateKeySigner);
        certificatesManager.putMerchant(this.merchantId, credentials,
                this.apiV3Key.getBytes(StandardCharsets.UTF_8));
        // 从证书管理器中获取verifier
        Verifier verifier = certificatesManager.getVerifier(this.merchantId);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(this.merchantId, this.certSerialNo, this.certPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));
        return builder.build();
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getCertSerialNo() {
        return this.certSerialNo;
    }

    public PrivateKey getCertPrivateKey() {
        return this.certPrivateKey;
    }

    public String getApiV3Key() {
        return this.apiV3Key;
    }

    public HttpClient getClient() {
        return this.client;
    }

    public CloseableHttpResponse execute(HttpUriRequest request) {
        try {
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            return (CloseableHttpResponse) this.client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CloseableHttpResponse post(String uri, Map<String, Object> body) {
        HttpPost httpPost = new HttpPost(uri);
        String json = JsonUtil.toJson(body);
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        return execute(httpPost);
    }

    public void close() {
        CertificatesManager.getInstance().stop();
    }

}
