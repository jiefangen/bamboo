package org.panda.tech.core.util.grouper;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 集合分组器
 *
 * @author fangen
 */
public class CollectionGrouper {

    public static <T> List<List<T>> groupByNumber(List<T> list, int number) {
        if (CollectionUtils.isEmpty(list) || number < 1) {
            throw new IllegalArgumentException();
        }
        return list.stream().collect(CustomCollectors.groupByNumber(number));
    }

}
