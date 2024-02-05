package org.panda.service.auth.infrastructure.security.service;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Auth服务管理器实现
 */
@Component
public class AuthServiceManagerImpl implements AuthServiceManager {

    @Override
    public String getAppName(String url) {
        if (StringUtils.isNotEmpty(url)) {
            return getAppNameByUrl(url);
        }
        return null;
    }

    private String getAppNameByUrl(String url) {
        int protocolIndex = url.indexOf("://");
        if (protocolIndex >= 0) { // 带有协议和域名的url
            url = url.substring(protocolIndex + 3);
            int slashIndex = url.indexOf(Strings.SLASH);
            url = url.substring(slashIndex + 1);
        }
        if (url.startsWith(Strings.SLASH)) { // 截取后确保不以/开头
            url = url.substring(1);
        }
        if (url.contains(Strings.SLASH)) {
            return url.substring(0, url.indexOf(Strings.SLASH));
        }
        return url;
    }

    @Override
    public List<String> getServices(String appName) {
        return null;
    }


    @Override
    public String getUri(HttpServletRequest request, String service) {
        int protocolIndex = service.indexOf("://");
        String Uri = Strings.EMPTY;
        if (protocolIndex >= 0) { // 带有协议和域名的url
            Uri = service.substring(protocolIndex + 3);
            int slashIndex = Uri.indexOf(Strings.SLASH);
            Uri = service.substring(slashIndex + 1);
        }
        if (Uri.startsWith(Strings.SLASH)) { // 截取后确保不以/开头
            Uri = Uri.substring(1);
        }
        return Uri;
    }

}
