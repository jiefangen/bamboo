package org.panda.common;

import com.alibaba.fastjson.JSONObject;
import org.panda.common.utils.UUIDUtil;
import org.panda.common.utils.collection.CustomCollectors;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用模块使用案例
 *
 * @author fangen
 * @since 2022/8/13
 */
public class CommonModule {
    public static void main(String[] args) {
        System.out.println(groupByNumberExample());
        System.out.println(UUIDUtil.randomUUID8() + " " + UUIDUtil.randomUUID32());
    }

    private static String groupByNumberExample() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 103; i++) {
            list.add(i);
        }
        List<List<Integer>> groupList = list.stream().collect(CustomCollectors.groupByNumber(10));
        return JSONObject.toJSONString(groupList);
    }

}
