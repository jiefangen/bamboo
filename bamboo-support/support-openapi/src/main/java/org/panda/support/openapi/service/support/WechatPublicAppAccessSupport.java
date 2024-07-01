package org.panda.support.openapi.service.support;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.EncryptUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.support.openapi.model.EncryptedData;
import org.panda.support.openapi.model.WechatUserDetail;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

/**
 * 微信公众平台（mp.weixin.qq.com）应用访问支持
 *
 * @author fangen
 */
public abstract class WechatPublicAppAccessSupport extends WechatAppAccessSupport {

    private static final long ACCESS_TOKEN_INTERVAL = 2 * Times.MS_ONE_HOUR; // 有效期，目前是7200秒之内的值

    private String accessToken;
    private long accessTokenExpiredTimestamp = 0L;
    private String jsApiTicket;
    private long jsApiTicketExpiredTimestamp = 0L;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public Map<String, Object> decryptData(String encryptedData, String iv, String sessionKey) {
        if (StringUtils.isBlank(encryptedData) || StringUtils.isBlank(iv)
                || StringUtils.isBlank(sessionKey)) {
            return null;
        }
        // 被加密的数据
        byte[] dataBytes = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        // 如果密钥不足16位就补足
        int base = 16;
        if (sessionKeyBytes.length % base != 0) {
            int groups = sessionKeyBytes.length / base
                    + (sessionKeyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(sessionKeyBytes, 0, temp, 0, sessionKeyBytes.length);
            sessionKeyBytes = temp;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec spec = new SecretKeySpec(sessionKeyBytes, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivBytes));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultBytes = cipher.doFinal(dataBytes);
            if (resultBytes != null && resultBytes.length > 0) {
                String json = new String(resultBytes, StandardCharsets.UTF_8);
                Map<String, Object> result = JsonUtil.json2Map(json);
                @SuppressWarnings("unchecked")
                Map<String, Object> watermark = (Map<String, Object>) result.get("watermark");
                if (watermark == null) {
                    return null;
                }
                if (!getAppId().equals(watermark.get("appid"))) {
                    return null;
                }
                return result;
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public Map<String, Object> decryptData(EncryptedData encryptedData, String sessionKey) {
        return decryptData(encryptedData.getEncryptedData(), encryptedData.getIv(), sessionKey);
    }

    public WechatUserDetail decryptUserDetail(String encryptedData, String iv, String sessionKey) {
        Map<String, Object> result = decryptData(encryptedData, iv, sessionKey);
        if (result != null) {
            WechatUserDetail userDetail = new WechatUserDetail();
            userDetail.setAppType(getAppType());
            userDetail.setAccessToken(accessToken);
            userDetail.setUnionId((String) result.get("unionid"));
            userDetail.setHeadImageUrl((String) result.get("avatarUrl"));
            userDetail.setNickname((String) result.get("nickName"));
            userDetail.setGender((Integer) result.get("gender"));
            userDetail.setCountry((String) result.get("country"));
            userDetail.setProvince((String) result.get("province"));
            userDetail.setCity((String) result.get("city"));
            return userDetail;
        }
        return null;
    }

    public String getUnionId(String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", getAccessToken());
        params.put("openid", openId);
        params.put("lang", Locale.getDefault().toString());
        Map<String, Object> result = get("/cgi-bin/user/info", params);
        return (String) result.get("unionid");
    }

    protected synchronized String getAccessToken() {
        long now = System.currentTimeMillis();
        if (this.accessToken == null || this.accessTokenExpiredTimestamp < now) {
            Map<String, Object> params = new HashMap<>();
            params.put("appid", getAppId());
            params.put("secret", getSecret());
            params.put("grant_type", "client_credential");
            Map<String, Object> result = get("/cgi-bin/token", params);
            this.accessToken = (String) result.get("access_token");
            this.accessTokenExpiredTimestamp = now + ACCESS_TOKEN_INTERVAL;
        }
        return this.accessToken;
    }

    protected synchronized String getJsApiTicket() {
        long now = System.currentTimeMillis();
        if (this.jsApiTicket == null || this.jsApiTicketExpiredTimestamp < now) {
            Map<String, Object> params = new HashMap<>();
            params.put("access_token", getAccessToken());
            params.put("type", "jsapi");
            Map<String, Object> result = get("/cgi-bin/ticket/getticket", params);
            this.jsApiTicket = (String) result.get("ticket");
            this.jsApiTicketExpiredTimestamp = now + ACCESS_TOKEN_INTERVAL; // 临时票据的过期间隔与AccessToken一致
        }
        return this.jsApiTicket;
    }

    public String signJsApiPage(String noncestr, long timestamp, String url) {
        String s = "jsapi_ticket=" + getJsApiTicket() + "&noncestr=" + noncestr + "&timestamp="
                + timestamp + "&url=" + url;
        return EncryptUtil.encryptBySha1(s);
    }

}
