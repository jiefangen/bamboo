package org.panda.business.example.test.tool;

import org.junit.jupiter.api.Test;
import org.panda.tech.core.web.util.IP2RegionUtil;

/**
 * 工具测试类
 *
 * @author fangen
 **/
public class ToolTest {

    @Test
    void getIPRegion() {
        // 120.25.220.23、47.243.14.168、203.156.219.9
        System.out.println(IP2RegionUtil.getIPRegion("203.156.219.9"));
        IP2RegionUtil ip2RegionUtil = new IP2RegionUtil();
        for (int i = 0; i < 10; i++) {
            System.out.println(ip2RegionUtil.getIPRegionCache("203.156.219.9"));
        }
    }
}
