package org.panda.service.payment.core.domain.gateway.wechat;

import com.wechat.pay.contrib.apache.httpclient.Validator;
import com.wechat.pay.contrib.apache.httpclient.auth.CertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.util.jackson.JsonUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 微信支付的平台证书（非商户API证书）管理器
 */
public class WechatPayPlatformCertManager {

    private static final String CERT_DOWNLOAD_URL = "https://api.mch.weixin.qq.com/v3/certificates";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final WechatPayClient client;
    private final Map<String, Certificate> certs = new Hashtable<>();
    private AesUtil decryptor;

    public WechatPayPlatformCertManager(WechatPayClient client) {
        this.client = client;
        this.decryptor = new AesUtil(client.getApiV3Key().getBytes(CHARSET));
    }

    @SuppressWarnings("unchecked")
    public Certificate getCert(String certSerialNo) throws IOException, GeneralSecurityException {
        Certificate certificate = this.certs.get(certSerialNo);
        if (certificate == null) {
            HttpGet httpGet = new HttpGet(CERT_DOWNLOAD_URL);
            CloseableHttpResponse response = this.client.execute(httpGet);
            String responseJson = EntityUtils.toString(response.getEntity());
            // 获取最新证书数据
            Map<String, Object> responseData = JsonUtil.json2Map(responseJson);
            List<Map<String, Object>> certList = (List<Map<String, Object>>) responseData.get("data");
            Map<String, Object> latestCert;
            if (certList.size() > 1) {
                certList.sort(Comparator.comparing(cert -> (String) cert.get("expire_time")));
                latestCert = certList.get(certList.size() - 1);
            } else {
                latestCert = certList.get(0);
            }
            Map<String, Object> encryptCertificate = (Map<String, Object>) latestCert.get("encrypt_certificate");
            // 解密取得证书
            String plainCertificate = decryptResource(encryptCertificate);
            X509Certificate x509Certificate = PemUtil.loadCertificate(
                    new ByteArrayInputStream(plainCertificate.getBytes(CHARSET)));
            // 验证证书合法性
            Verifier verifier = new CertificatesVerifier(List.of(x509Certificate));
            Validator validator = new WechatPay2Validator(verifier);
            if (validator.validate(response)) {
                certificate = x509Certificate;
                this.certs.put(certSerialNo, certificate);
            }
        }
        return certificate;
    }

    public String decryptResource(Map<String, Object> resource) throws GeneralSecurityException {
        String associatedData = (String) resource.get("associated_data");
        String nonce = (String) resource.get("nonce");
        String ciphertext = (String) resource.get("ciphertext");
        return this.decryptor.decryptToString(associatedData.getBytes(CHARSET), nonce.getBytes(CHARSET), ciphertext);
    }

}
