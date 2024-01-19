package org.panda.tech.data;

import org.panda.tech.data.model.entity.relation.Relation;

import java.io.Serializable;

/**
 * 关系数据访问仓库扩展
 *
 * @param <T> 关系类型
 * @param <L> 左标识类型
 * @param <R> 右标识类型
 */
public interface RelationRepox<T extends Relation<L, R>, L extends Serializable, R extends Serializable>
        extends Repox<T> {

    T find(L leftId, R rightId);

    boolean exists(L leftId, R rightId);

    void delete(L leftId, R rightId);

    /**
     * 递增指定关系的指定数值属性值
     *
     * @param leftId       左标识
     * @param rightId      右标识
     * @param propertyName 数值属性名
     * @param step         递增的值，为负值即表示递减
     * @param limit        增减后允许的最大/最小值，设定以避免数值超限
     * @param <N>          数值类型
     * @return 关系
     */
    default <N extends Number> T increaseNumber(L leftId, R rightId, String propertyName, N step, N limit) {
        throw new UnsupportedOperationException();
    }

}
