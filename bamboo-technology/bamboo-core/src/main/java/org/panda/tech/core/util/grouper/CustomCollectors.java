package org.panda.tech.core.util.grouper;

import java.util.List;
import java.util.stream.Collector;

/**
 * 自定义集合分组器
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
        return new NumberCollector<>(number);
    }
}
