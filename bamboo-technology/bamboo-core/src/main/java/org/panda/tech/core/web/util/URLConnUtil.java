package org.panda.tech.core.web.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.io.IOUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * URLConnection客户端工具类
 * 建议场景：简单易用，适用于基本的HTTP请求和响应操作
 **/
public class URLConnUtil {

    private static URLConnection getURLConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(15000);
        return connection;
    }

    /**
     * 从后台向指定URL地址发送GET方式请求，获取响应结果
     *
     * @param url      URL地址
     * @param params   请求参数
     * @param encoding 参数值的字符集编码
     * @return 响应结果
     */
    public static String requestByGet(String url, Map<String, Object> params, String encoding) {
        if (StringUtils.isBlank(encoding)) {
            encoding = Strings.ENCODING_UTF8;
        }
        url = NetUtil.mergeParams(url, params, encoding);
        String result = Strings.EMPTY;
        InputStream in = null;
        try {
            URLConnection connection = getURLConnection(url);
            in = connection.getInputStream();
            result = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            LogUtil.error(URLConnUtil.class, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(URLConnUtil.class, e);
                }
            }
        }
        return result;
    }

    private static String getQueryString(Map<String, Object> params) {
        String result = NetUtil.mergeParams(Strings.EMPTY, params, Strings.ENCODING_UTF8);
        if (result.length() > 0) {
            return result.substring(1); // 去掉首部问号
        }
        return Strings.EMPTY;
    }

    public static String requestByPost(String url, Map<String, Object> params, String encoding) {
        InputStream in = null;
        PrintWriter out = null;
        String response = Strings.EMPTY;
        try {
            URLConnection connection = getURLConnection(url);
            if (StringUtils.isBlank(encoding)) {
                encoding = Strings.ENCODING_UTF8;
            }
            connection.setRequestProperty("contentType", "text/html;charset=" + encoding);
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), encoding));
            out.write(getQueryString(params));
            out.flush();
            in = connection.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            response = new String(b, encoding);
        } catch (IOException e) {
            LogUtil.error(URLConnUtil.class, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(URLConnUtil.class, e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    LogUtil.error(URLConnUtil.class, e);
                }
            }
        }
        return response;
    }

    /**
     * 下载指定URL和参数表示的资源到指定本地文件
     *
     * @param url       下载资源链接
     * @param params    下载资源链接的参数
     * @param localFile 本地文件
     */
    public static void download(String url, Map<String, Object> params, File localFile) {
        url = NetUtil.mergeParams(url, params, Strings.ENCODING_UTF8);
        InputStream in = null;
        OutputStream out = null;
        try {
            URL urlObj = new URL(url);
            in = urlObj.openStream();
            IOUtil.createFile(localFile);
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            IOUtils.copy(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            LogUtil.error(URLConnUtil.class, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.error(URLConnUtil.class, e);
            }
        }
    }
}
