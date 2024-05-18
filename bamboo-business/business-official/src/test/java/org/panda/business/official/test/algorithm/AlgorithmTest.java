package org.panda.business.official.test.algorithm;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.algorithm.AlgoChecksum;
import org.panda.tech.core.web.util.IP2RegionUtil;

/**
 * 算法测试类
 *
 * @author fangen
 **/
public class AlgorithmTest {

    @Test
    void algoChecksum() {
        AlgoChecksum algoChecksum = new AlgoChecksum(10, 10);
        String checkResult = algoChecksum.visit("Hello");
        System.out.println(checkResult);
    }

    @Test
    void getIPRegion() {
        // 120.25.220.23、47.243.14.168
        System.out.println(IP2RegionUtil.getIPRegion("47.243.14.168"));
        IP2RegionUtil ip2RegionUtil = new IP2RegionUtil();
        for (int i = 0; i < 10; i++) {
            System.out.println(ip2RegionUtil.getIPRegionCache("120.25.220.23"));
        }
    }
}
