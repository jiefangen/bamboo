package org.panda.support.openapi.service.support;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binate;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.support.openapi.model.WechatAppType;
import org.panda.support.openapi.model.WechatUserDetail;
import org.panda.tech.core.util.http.HttpClientUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信应用访问支持
 *
 * @author fangen
 */
public abstract class WechatAppAccessSupport implements WechatAppAccessor {

    protected static final String HOST = "https://api.weixin.qq.com";

    protected Map<String, Object> get(String uri, Map<String, Object> params) {
        try {
            Binate<Integer, String> response = HttpClientUtil.request(HttpMethod.GET, HOST + uri, params, null,
                    null, Strings.ENCODING_UTF8);
            if (response != null) {
                String body = response.getRight();
                return JsonUtil.json2Map(body);
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return new HashMap<>();
    }

    protected Map<String, Object> post(String url, Map<String, Object> params) {
        try {
            String encoding = Strings.ENCODING_UTF8;
            HttpPost post = new HttpPost(HOST + url);
            // 发送微信公众号模板消息需以下写法
            post.setEntity(new StringEntity(JsonUtil.toJson(params),
                    ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), encoding)));
            String content = HttpClientUtil.execute(post, encoding);
            return JsonUtil.json2Map(content);
        } catch (Exception e) {
            LogUtil.error(getClass(), e);
        }
        return Collections.emptyMap();
    }

    protected Map<String, Object> postFormData(String url, InputStream in, String mimeType) {
        HttpPost request = new HttpPost(HOST + url);
        request.addHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        try {
            byte[] content = IOUtils.toByteArray(in);
            request.setEntity(new ByteArrayEntity(content, ContentType.create(mimeType)));
            CloseableHttpResponse response = HttpClientUtil.HTTP_CLIENT.execute(request);
            String json = EntityUtils.toString(response.getEntity(), Strings.ENCODING_UTF8);
            return JsonUtil.json2Map(json);
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
        return Collections.emptyMap();
    }

    /**
     * @return 应用类型
     */
    public abstract WechatAppType getAppType();

    /**
     * @return 应用id
     */
    public abstract String getAppId();

    /**
     * @return 访问秘钥
     */
    protected abstract String getSecret();

    public WechatUserDetail getUserDetail(String openid, String accessToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("openid", openid);
        params.put("access_token", accessToken);
        Map<String, Object> result = get("/sns/userinfo", params);
        if (openid.equals(result.get("openid"))) {
            WechatUserDetail user = new WechatUserDetail();
            user.setAppType(getAppType());
            user.setOpenid(openid);
            user.setAccessToken(accessToken);
            user.setUnionId((String) result.get("unionid"));
            user.setHeadImageUrl((String) result.get("avatarUrl"));
            user.setNickname((String) result.get("nickName"));
            user.setGender((Integer) result.get("gender"));
            user.setCountry((String) result.get("country"));
            user.setProvince((String) result.get("province"));
            user.setCity((String) result.get("city"));
            return user;
        }
        return null;
    }
}
