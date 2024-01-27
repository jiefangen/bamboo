package org.panda.tech.security.cas.util;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.util.NetUtil;

public class CasUtil {

    private CasUtil() {
    }

    public static String getServicePrefixByAppName(String appName) {
        return Strings.LEFT_SQUARE_BRACKET + appName + Strings.RIGHT_SQUARE_BRACKET;
    }

    public static String appendService(String url, String serviceParameterName, String service) {
        String regex = "[?&]" + serviceParameterName + Strings.EQUAL + NetUtil.encode(Strings.LEFT_SQUARE_BRACKET)
                + "\\w+" + NetUtil.encode(Strings.RIGHT_SQUARE_BRACKET);
        int index = StringUtil.regexFirstEnd(url, regex); // 匹配字符最后一位的后一位
        if (index >= 0) {
            // 以[appName]结尾，则直接附加service即可
            if (index == url.length()) {
                return url + NetUtil.encode(service);
            }
            // [appName]之后为&符号，说明还有其它参数，则将service插入[appName]和&之间
            if (Strings.AND.equals(url.substring(index, index + 1))) {
                return url.substring(0, index) + NetUtil.encode(service) + url.substring(index);
            }
        }
        return url;
    }

}
