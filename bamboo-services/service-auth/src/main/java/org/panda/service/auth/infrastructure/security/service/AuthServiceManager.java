package org.panda.service.auth.infrastructure.security.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Auth服务管理器
 */
public interface AuthServiceManager {

    String getAppName(String url);

    List<String> getServices(String appName);

    String getUri(HttpServletRequest request, String service);

}
