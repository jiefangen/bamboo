package org.panda.tech.core.util.grouper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 元素个数集合分组器
 *
 * @param <T> 元素类型
 */
public class NumberCollector<T> implements Collector<T, List<List<T>>, List<List<T>>> {
    // 每组的个数
    private final int number;

    public NumberCollector(int number) {
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
            } else {
                List<T> last = list.get(list.size() - 1);
                if (last.size() < number){
                    last.add(item);
                } else {
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
