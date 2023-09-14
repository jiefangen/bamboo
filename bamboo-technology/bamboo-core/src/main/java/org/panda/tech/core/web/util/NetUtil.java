package org.panda.tech.core.web.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.io.IOUtil;
import org.panda.bamboo.common.util.lang.MathUtil;
import org.panda.bamboo.common.util.lang.StringUtil;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 网络工具类
 *
 * @author fangen
 */
public class NetUtil {

    public static final String LOCAL_HOST_NAME = "localhost";
    public static final String LOCAL_IP_V4 = "127.0.0.1";
    public static final String LOCAL_IP_V6 = "0:0:0:0:0:0:0:1";
    public static final String PROTOCOL_HTTP = "http://";
    public static final String PROTOCOL_HTTPS = "https://";

    private NetUtil() {
    }

    /**
     * 获取指定主机名（域名）对应的IP地址
     *
     * @param host 主机名（域名）
     * @return IP地址
     */
    public static String getIpByHost(String host) {
        if (StringUtil.isIp(host)) {
            return host;
        }
        StringBuilder s = new StringBuilder();
        try {
            InetAddress address = InetAddress.getByName(host);
            for (byte b : address.getAddress()) {
                s.append(b & 0xff).append(Strings.DOT);
            }
            if (s.length() > 0) {
                s = new StringBuilder(s.substring(0, s.length() - 1));
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        return s.toString();
    }

    /**
     * 获取本机Host地址
     *
     * @return 本机Host地址
     */
    public static String getLocalHost() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                for (Enumeration<InetAddress> ias = ni.getInetAddresses(); ias.hasMoreElements(); ) {
                    InetAddress inetAddr = ias.nextElement();
                    if (!inetAddr.isLoopbackAddress() && inetAddr instanceof Inet4Address) {
                        return inetAddr.getHostAddress();
                    }
                }
            }
            // 如果在循环中没有找到IPv4地址，可以回退到原始方案
            InetAddress localhost = InetAddress.getLocalHost();
            if (localhost instanceof Inet4Address) {
                return localhost.getHostAddress();
            }
        } catch (Exception e) {
            LogUtil.error(NetUtil.class, e);
        }
        return LOCAL_IP_V4;
    }

    public static List<String> getLocalIntranetIps() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    String ip = ias.nextElement().getHostAddress();
                    if (NetUtil.isIntranetIp(ip) && !LOCAL_IP_V4.equals(ip) && !LOCAL_IP_V6.equals(ip)) {
                        ips.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            LogUtil.error(NetUtil.class, e);
        }
        return ips;
    }

    /**
     * 获取本机内网IP地址
     *
     * @return 本机网卡IP地址
     */
    public static String getIntranetIp() {
        List<String> ips = getLocalIntranetIps();
        return ips.size() > 0 ? ips.get(0) : LOCAL_IP_V4;
    }

    /**
     * 获取指定ip地址字符串转换成的IPv4网络地址对象。如果无法转换则返回null
     *
     * @param ip ip地址字符串
     * @return IPv4网络地址对象
     */
    public static Inet4Address getInet4Address(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (address instanceof Inet4Address) {
                return (Inet4Address) address;
            }
        } catch (UnknownHostException ignored) {
        }
        return null;
    }

    public static String getTopDomain(String host) {
        if (StringUtil.isIp(host)) {
            return null;
        }
        String[] array = host.split("\\.");
        if (array.length >= 2) {
            return array[array.length - 2] + Strings.DOT + array[array.length - 1];
        }
        return host;
    }

    /**
     * 判断指定字符串是否内网IP地址
     *
     * @param s 字符串
     * @return true if 指定字符串是内网IP地址, otherwise false
     */
    public static boolean isIntranetIp(String s) {
        if (StringUtil.isIp(s)) {
            if (isLocalHost(s) || s.startsWith("192.168.") || s.startsWith("10.")) {
                return true;
            } else if (s.startsWith("172.")) { // 172.16-172.31网段
                String seg = s.substring(4, s.indexOf('.', 4)); // 取第二节
                int value = MathUtil.parseInt(seg);
                return 16 <= value && value <= 31;
            }
        }
        return false;
    }

    /**
     * 判断指定主机地址是否本机地址
     *
     * @param host 主机地址
     * @return 指定主机地址是否本机地址
     */
    public static boolean isLocalHost(String host) {
        return LOCAL_HOST_NAME.equals(host) || LOCAL_IP_V4.equals(host) || LOCAL_IP_V6.equals(host);
    }

    /**
     * 判断指定网络地址是否内网地址
     *
     * @param address 网络地址
     * @return 指定网络地址是否内网地址
     */
    public static boolean isIntranetAddress(InetAddress address) {
        byte[] b = address.getAddress();
        // 暂只考虑IPv4
        return b.length == 4 && ((b[0] == 192 && b[1] == 168) || b[0] == 10 || (b[0] == 172 && b[1] >= 16 && b[1] <= 31)
                || (b[0] == 127 && b[1] == 0 && b[2] == 0 && b[3] == 1));
    }

    /**
     * 获取指定IP地址的整数表达形式
     *
     * @param address IP地址
     * @return 整数表达形式
     */
    public static int intValueOf(InetAddress address) {
        // IPv4和IPv6的hashCode()即为其整数表达形式，本方法向调用者屏蔽该逻辑
        return address.hashCode();
    }

    /**
     * 将指定参数集合转换为参数字符串，形如: a=1&amp;b=true
     *
     * @param params   参数集合
     * @param encoding 字符编码
     * @return 参数字符串
     */
    @SuppressWarnings("unchecked")
    public static String map2ParamString(Map<String, Object> params, String encoding) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof Collection) {
                    for (Object o : (Collection<Object>) value) {
                        result.append(key).append(Strings.EQUAL).append(encodeParam(o, encoding)).append(Strings.AND);
                    }
                } else if (value instanceof Object[]) {
                    for (Object o : (Object[]) value) {
                        result.append(key).append(Strings.EQUAL).append(encodeParam(o, encoding)).append(Strings.AND);
                    }
                } else {
                    result.append(key).append(Strings.EQUAL).append(encodeParam(value, encoding)).append(Strings.AND);
                }
            }
        }
        if (result.length() > 0) {
            result.delete(result.length() - Strings.AND.length(), result.length());
        }
        return result.toString();
    }

    private static String encodeParam(Object param, String encoding) {
        if (StringUtils.isNotBlank(encoding)) {
            try {
                return URLEncoder.encode(param.toString(), encoding);
            } catch (UnsupportedEncodingException e) {
            }
        }
        // 编码为空或不被支持，则不做编码转换
        return param.toString();
    }

    /**
     * 将指定参数字符串（形如: a=1&amp;b=true）转换为Map参数集合
     *
     * @param paramString 参数字符串
     * @return 参数集合
     */
    public static Map<String, Object> paramString2Map(String paramString) {
        Map<String, Object> map = new HashMap<>();
        String[] pairArray = paramString.split(Strings.AND);
        for (String pair : pairArray) {
            String[] entry = pair.split(Strings.EQUAL);
            if (entry.length == 2) {
                String key = entry[0];
                Object value = map.get(key);
                if (value instanceof Collection) {
                    @SuppressWarnings("unchecked")
                    Collection<Object> collection = (Collection<Object>) value;
                    collection.add(entry[1]);
                } else if (value != null) {
                    Collection<Object> collection = new ArrayList<>();
                    collection.add(entry[1]);
                    map.put(key, collection);
                } else {
                    map.put(key, entry[1]);
                }
            }
        }
        return map;
    }

    public static String mergeParam(String url, String paramName, Object paramValue) {
        if (paramName != null && paramValue != null) {
            if (url.contains(Strings.QUESTION)) {
                url += Strings.AND;
            } else {
                url += Strings.QUESTION;
            }
            url += paramName + Strings.EQUAL + paramValue;
        }
        return url;
    }

    /**
     * 将指定参数集合中的参数与指定URL中的参数合并，返回新的URL
     *
     * @param url      URL
     * @param params   参数集合
     * @param encoding 字符编码
     * @return 合并之后的新URL
     */
    public static String mergeParams(String url, Map<String, Object> params, String encoding) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        if (url.contains(Strings.QUESTION)) {
            url += Strings.AND;
        } else {
            url += Strings.QUESTION;
        }
        return url + map2ParamString(params, encoding);
    }

    private static String getQueryString(Map<String, Object> params) {
        String result = mergeParams(Strings.EMPTY, params, Strings.ENCODING_UTF8);
        if (result.length() > 0) {
            return result.substring(1); // 去掉首部问号
        }
        return Strings.EMPTY;
    }

    public static String removeParams(String url, Collection<String> paramNames) {
        if (paramNames.size() > 0) {
            int index = url.indexOf(Strings.QUESTION);
            if (index >= 0) {
                // 去参数字符串且以&开头和结尾，以便于处理
                String paramString = Strings.AND + url.substring(index + 1) + Strings.AND;
                for (String paramName : paramNames) {
                    String pattern = Strings.AND + paramName + Strings.EQUAL + "[^&=#?]+" + Strings.AND;
                    paramString = paramString.replaceAll(pattern, Strings.AND);
                }
                paramString = paramString.substring(1); // 去掉开头的&
                if (paramString.length() == 0) {
                    url = url.substring(0, index);
                } else {
                    url = url.substring(0, index + 1) + paramString;
                }
            } // 不包含参数的URL无需处理
        }
        return url;
    }

    /**
     * 下载指定URL和参数表示的资源到指定本地文件
     *
     * @param url       下载资源链接
     * @param params    下载资源链接的参数
     * @param localFile 本地文件
     */
    public static void download(String url, Map<String, Object> params, File localFile) {
        url = mergeParams(url, params, Strings.ENCODING_UTF8);
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
            LogUtil.error(NetUtil.class, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.error(NetUtil.class, e);
            }
        }
    }

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
        url = mergeParams(url, params, encoding);
        String result = Strings.EMPTY;
        InputStream in = null;
        try {
            URLConnection connection = getURLConnection(url);
            in = connection.getInputStream();
            result = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            LogUtil.error(NetUtil.class, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(NetUtil.class, e);
                }
            }
        }
        return result;
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
            LogUtil.error(NetUtil.class, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(NetUtil.class, e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    LogUtil.error(NetUtil.class, e);
                }
            }
        }
        return response;
    }

    public static boolean isRelativeUrl(String url) {
        return url.startsWith(Strings.SLASH) && !url.startsWith(Strings.DOUBLE_SLASH);
    }

    /**
     * 标准化URL地址。所谓标准URL即：所有斜杠均为/，以/开头，不以/结尾
     *
     * @param url URL
     * @return 标准化后的URL
     */
    public static String standardizeUrl(String url) {
        if (isRelativeUrl(url)) {
            url = url.replace('\\', '/');
            if (!url.startsWith(Strings.SLASH)) {
                url = Strings.SLASH + url;
            }
            if (Strings.SLASH.equals(url)) {
                return url;
            }
        }
        if (url.endsWith(Strings.SLASH)) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 标准化指定URL中的协议，确保返回的URL包含协议，如果指定URL未包含协议，则返回包含有指定默认协议的URL
     *
     * @param url             URL
     * @param defaultProtocol 默认协议，如："http"
     * @return 包含有协议的URL，如果输入的URL为相对路径，则原样返回
     */
    public static String standardizeUrlWithProtocol(String url, String defaultProtocol) {
        if (!url.contains("://")) {
            if (url.startsWith(Strings.DOUBLE_SLASH)) {
                url = defaultProtocol + Strings.COLON + url;
            } else if (!url.startsWith(Strings.SLASH)) {
                url = defaultProtocol + "://" + url;
            }
            // 斜杠开头的为相对URL，不作处理
        }
        if (url.endsWith(Strings.SLASH)) { // 确保不以斜杠结尾
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 标准化指定URL，当该URL不包含协议时，返回包含HTTP协议的URL
     *
     * @param url URL
     * @return 包含有协议（默认为HTTP协议）的URL
     */
    public static String standardizeHttpUrl(String url) {
        return standardizeUrlWithProtocol(url, "http");
    }

    /**
     * 从指定URL中截取请求action部分，即请求url中去掉参数和请求后缀之后的部分
     *
     * @param url 请求url
     * @return 请求action
     */
    public static String getAction(String url) {
        int questionIndex = url.indexOf(Strings.QUESTION);
        if (questionIndex >= 0) {
            url = url.substring(0, questionIndex);
        }
        int dotIndex = url.lastIndexOf(Strings.DOT);
        if (dotIndex >= 0) {
            int slashIndex = url.lastIndexOf(Strings.SLASH);
            if (dotIndex > slashIndex) { // .在最后一个/之后，才是扩展名
                url = url.substring(0, dotIndex);
            }
        }
        return url;
    }

    /**
     * 从指定URL地址中获取主机地址（域名/IP[:端口]）
     *
     * @param url          URL地址
     * @param portPossible 是否带上可能的端口号
     * @return 主机地址
     */
    public static String getHost(String url, boolean portPossible) {
        int index = url.indexOf("://");
        if (index >= 0) {
            url = url.substring(index + 3);
        } else if (url.startsWith(Strings.DOUBLE_SLASH)) { // 以//开头是不包含协议但包含主机地址的链接
            url = url.substring(2);
        } else { // 其它情况下URL中不包含主机地址
            return null;
        }
        index = url.indexOf(Strings.SLASH);
        if (index >= 0) {
            url = url.substring(0, index);
        }
        index = url.indexOf(Strings.COLON);
        if (index >= 0 && portPossible) { // 即使需要带上可能的端口号，但80和443端口必须忽略
            String portString = url.substring(index + 1);
            int port = MathUtil.parseInt(portString);
            if (port != 80 && port != 443) {
                index = -1;
            }
        }
        if (index >= 0) {
            url = url.substring(0, index);
        }
        return url;
    }

    public static String getSubDomain(String url, int topDomainLevel) {
        url = getHost(url, false);
        if (StringUtil.isIp(url)) {
            return null;
        }
        String[] domains = url.split("\\.");
        if (topDomainLevel < 2) {
            topDomainLevel = 2;
        }
        if (domains.length > topDomainLevel) {
            StringBuilder domain = new StringBuilder(domains[0]);
            for (int i = 1; i < domains.length - topDomainLevel; i++) {
                domain.append(Strings.DOT).append(domains[i]);
            }
            return domain.toString();
        }
        return null;
    }

    public static boolean isHttpUrl(String url, boolean acceptHttps) {
        if (url.startsWith(PROTOCOL_HTTP)) {
            return true;
        }
        if (acceptHttps && url.startsWith(PROTOCOL_HTTPS)) {
            return true;
        }
        return false;
    }

}
