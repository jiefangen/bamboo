package org.panda.bamboo.common.collectors;

import java.util.*;

import java.util.stream.Collector;

/**
 * 自定义Collector集合分组器
 *
 * @author fangen
 * @since 2022/8/10
 */
public class CustomCollectors {
    /**
     * 按照特定元素个数平均分组
     *
     * @param number 分组个数
     * @param <T> 集合元素类型
     * @return 分组后的集合
     */
    public static <T> Collector<T, List<List<T>>, List<List<T>>> groupByNumber(int number){
        return new NumberCollector<T>(number);
    }
}
