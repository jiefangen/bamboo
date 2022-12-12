package org.panda.bamboo.common;

import com.alibaba.fastjson.JSONObject;

import org.panda.bamboo.common.collection.CustomCollectors;

import java.util.ArrayList;
import java.util.List;

/**
 * 技术框架通用模块
 *
 * @author fangen
 */
public class CommonModule {
    public static void main(String[] args) {
        System.out.println(groupByNumberExample());
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
