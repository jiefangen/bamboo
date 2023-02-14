package org.panda.core.algorithm.grouper;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.core.exception.ParamException;

import java.util.List;

/**
 * 集合分组算法
 *
 * @author fangen
 */
public class CollectionGrouper {

    public static <T> List<List<T>> groupByNumber(List<T> list, int number) {
        if (CollectionUtils.isEmpty(list) || number < 1) {
            throw new ParamException();
        }
        List<List<T>> groupList = list.stream().collect(CustomCollectors.groupByNumber(number));
        return groupList;
    }

}
