package org.panda.core.algorithm;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.panda.core.utils.grouper.CollectionGrouper;

import java.util.ArrayList;
import java.util.List;

public class CollectionGrouperTest {

    @Test
    void defaultTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            list.add(i);
        }
        List<List<Integer>> groupList = CollectionGrouper.groupByNumber(list, 10);
        System.out.println(JSONObject.toJSONString(groupList));
    }

}
