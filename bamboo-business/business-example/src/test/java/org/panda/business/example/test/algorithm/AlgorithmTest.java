package org.panda.business.example.test.algorithm;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.algorithm.AlgoChecksum;
import org.panda.bamboo.common.util.lang.StringUtil;

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
    void generateKey() {
        String key = StringUtil.randomNormalMixeds(32);
        System.out.println(key);
    }
}
