package org.panda.tech.core.util;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.bamboo.common.model.tuple.Binate;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.spec.HttpRequestMethod;
import org.panda.tech.core.web.util.NetUtil;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Http客户端工具类
 */
public class HttpClientUtil {

    public static final CloseableHttpClient CLIENT = HttpClientBuilder.create().build();

    private HttpClientUtil() {
    }

    private static CloseableHttpResponse execute(HttpRequestMethod method, String url, Map<String, Object> params,
                                                 Map<String, String> headers, String encoding) throws Exception {
        HttpRequestBase request;
        switch (method) {
            case GET:
                request = new HttpGet(NetUtil.mergeParams(url, params, encoding));
                break;
            case POST:
                HttpPost post = new HttpPost(url);
                if (params != null) {
                    // 发送微信公众号模板消息需以下写法
                    post.setEntity(new StringEntity(JsonUtil.toJson(params),
                            ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), encoding)));
                }
                request = post;
                break;
            default:
                request = null;
        }
        if (request != null) {
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }
            return CLIENT.execute(request);
        }
        return null;
    }

    public static Binary<Integer, String> request(HttpRequestMethod method, String url, Map<String, Object> params,
                                                  Map<String, String> headers, String encoding) throws Exception {
        CloseableHttpResponse response = execute(method, url, params, headers, encoding);
        if (response != null) {
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                String content;
                // 301和302重定向状态码，将重定向目标地址作为内容返回
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    Header header = response.getFirstHeader(HttpHeaders.LOCATION);
                    content = header.getValue();
                } else {
                    content = EntityUtils.toString(response.getEntity(), encoding);
                }
                return new Binary<>(statusCode, content);
            } finally {
                // 确保关闭请求连接
                response.close();
            }
        }
        return null;
    }

    public static Binate<Integer, String> requestByGet(String url, Map<String, Object> params,
                                                       Map<String, String> headers) throws Exception {
        return request(HttpRequestMethod.GET, url, params, headers, Strings.ENCODING_UTF8);
    }

    public static Binate<Integer, String> requestByPost(String url, Map<String, Object> params,
            Map<String, String> headers) throws Exception {
        return request(HttpRequestMethod.POST, url, params, headers, Strings.ENCODING_UTF8);
    }

    public static InputStream getImageByPostJson(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        try {
            StringEntity entity = new StringEntity(JsonUtil.toJson(params));
            entity.setContentType(MediaType.IMAGE_PNG_VALUE); // png比jpg具有更大的适应性，固定为png
            httpPost.setEntity(entity);
            HttpResponse response = CLIENT.execute(httpPost);
            return response.getEntity().getContent();
        } catch (Exception e) {
            LogUtil.error(HttpClientUtil.class, e);
        }
        return null;
    }

    public static void download(String url, Map<String, Object> params, Map<String, String> headers,
            BiConsumer<HttpEntity, Map<String, String>> consumer) throws IOException {
        try {
            CloseableHttpResponse response = execute(HttpRequestMethod.GET, url, params, headers,
                    Strings.ENCODING_UTF8);
            if (response != null) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    Map<String, String> responseHeaders = new HashMap<>();
                    for (Header header : response.getAllHeaders()) {
                        responseHeaders.put(header.getName(), header.getValue());
                    }
                    consumer.accept(response.getEntity(), responseHeaders);
                } else {
                    LogUtil.error(HttpClientUtil.class,
                            "====== " + statusLine + HttpRequestMethod.GET.name() + Strings.SPACE + url);
                }
            }
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            LogUtil.error(HttpClientUtil.class, e);
        }
    }

}
