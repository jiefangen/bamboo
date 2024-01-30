package org.panda.business.official.test.algorithm;

import org.junit.jupiter.api.Test;
import org.panda.bamboo.common.util.algorithm.AlgoChecksum;

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

}
