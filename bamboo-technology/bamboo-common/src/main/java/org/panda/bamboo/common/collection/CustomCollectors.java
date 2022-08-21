package org.panda.bamboo.common.collection;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
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
     * @return
     */
    public static <T> Collector<T, List<List<T>>, List<List<T>>> groupByNumber(int number){
        return new NumberCollectorImpl(number);
    }

    /**
     * 个数分组器
     * @param <T>
     */
    static class NumberCollectorImpl<T> implements Collector<T, List<List<T>>, List<List<T>>> {
        // 每组的个数
        private int number;

        public NumberCollectorImpl(int number) {
            this.number = number;
        }

        @Override
        public Supplier<List<List<T>>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<List<T>>, T> accumulator() {
            return (list, item) -> {
                if (list.isEmpty()){
                    list.add(this.createNewList(item));
                }else {
                    List<T> last = list.get(list.size() - 1);
                    if (last.size() < number){
                        last.add(item);
                    }else{
                        list.add(this.createNewList(item));
                    }
                }
            };
        }

        @Override
        public BinaryOperator<List<List<T>>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            };
        }

        @Override
        public Function<List<List<T>>, List<List<T>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
        }

        private List<T> createNewList(T item){
            List<T> newOne = new ArrayList<>();
            newOne.add(item);
            return newOne;
        }
    }

}
