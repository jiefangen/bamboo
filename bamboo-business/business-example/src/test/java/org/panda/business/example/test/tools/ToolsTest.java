package org.panda.business.example.test.tools;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.web.util.IP2RegionUtil;

/**
 * 工具测试类
 *
 * @author fangen
 **/
public class ToolsTest {

    private final IP2RegionUtil ip2RegionUtil = new IP2RegionUtil();

    @Test
    void getIPRegion() {
        // 127.0.0.1（本地）、198.19.249.3（内网）
        // 203.156.219.9（上海电信）、47.243.14.168（香港）、120.25.220.23（阿里云）
        LogUtil.debug(getClass(), IP2RegionUtil.getIPRegion("127.0.0.1"));
        LogUtil.debug(getClass(), IP2RegionUtil.getIPRegion("198.19.249.3"));
        // IP数据缓存
        LogUtil.info(getClass(), ip2RegionUtil.getIPRegionCache("203.156.219.9"));
        LogUtil.warn(getClass(), ip2RegionUtil.getIPRegionCache("47.243.14.168"));
        LogUtil.error(getClass(), ip2RegionUtil.getIPRegionCache("120.25.220.23"));
    }
}
