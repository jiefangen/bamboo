package org.panda.tech.core.util;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.model.tuple.Binary;
import org.panda.bamboo.common.model.tuple.Binate;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.tech.core.spec.http.HttpRequestMethod;
import org.panda.tech.core.web.util.NetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Http客户端工具类
 */
public class HttpClientUtil {

    public static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    private HttpClientUtil() {
    }

    private static CloseableHttpResponse execute(HttpRequestMethod method, String url, Map<String, Object> params,
                                                 Map<String, String> headers, String encoding) throws Exception {
        HttpRequestBase request = null;
        switch (method) {
            case GET:
                request = new HttpGet(NetUtil.mergeParams(url, params, encoding));
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    // 发送微信公众号模板消息需以下写法
                    httpPost.setEntity(new StringEntity(JsonUtil.toJson(params),
                            ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), encoding)));
                }
                request = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                if (params != null) {
                    httpPut.setEntity(new StringEntity(JsonUtil.toJson(params),
                            ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), encoding)));
                }
                request = httpPut;
                break;
            case DELETE:
                request = new HttpDelete(url);
                break;
        }
        if (request != null) {
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }
            // 设置httpclient请求超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000) // 连接请求超时时间
                    .setConnectTimeout(6000) // 连接超时时间
                    .setSocketTimeout(15000) // 读取超时时间
                    .build();
            request.setConfig(requestConfig);
            return HTTP_CLIENT.execute(request);
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

    public static void download(String url, Map<String, Object> params, Map<String, String> headers,
            BiConsumer<HttpEntity, Map<String, String>> consumer) throws IOException {
        try {
            CloseableHttpResponse response = execute(HttpRequestMethod.GET, url, params, headers, Strings.ENCODING_UTF8);
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
