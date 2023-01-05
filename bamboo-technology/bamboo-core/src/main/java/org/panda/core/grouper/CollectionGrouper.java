package org.panda.core.grouper;

import org.apache.commons.collections4.CollectionUtils;
import org.panda.core.exception.ParameterException;

import java.util.List;

public class CollectionGrouper {

    public static <T> List<List<T>> groupByNumber(List<T> list, int number) {
        if (CollectionUtils.isEmpty(list) || number < 1) {
            throw new ParameterException();
        }
        List<List<T>> groupList = list.stream().collect(CustomCollectors.groupByNumber(number));
        return groupList;
    }

}
