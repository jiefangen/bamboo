package org.panda.tech.core.web.mvc.servlet.http;

import java.util.Map;

/**
 * http请求数据提供者
 */
public interface HttpRequestDataProvider {

    String getHeader(String name);

    Map<String, String> getHeaders();

    String getParameter(String name);

    String[] getParameterArray(String name);

    Map<String, Object> getParameters();

    String getBody();

}
