package org.panda.tech.data;

import org.panda.tech.data.model.entity.unity.Unity;

import java.io.Serializable;

/**
 * 单体数据访问仓库扩展
 *
 * @param <T> 单体类型
 * @param <K> 单体标识类型
 */
public interface UnityRepox<T extends Unity<K>, K extends Serializable> extends Repox<T> {

    /**
     * 递增指定实体的指定数值属性值
     *
     * @param id           单体标识
     * @param propertyName 数值属性名
     * @param step         递增的值，为负值即表示递减
     * @param limit        增减后允许的最大/最小值，设定以避免数值超限
     * @param <N>          值类型
     * @return 单体
     */
    default <N extends Number> T increaseNumber(K id, String propertyName, N step, N limit) {
        throw new UnsupportedOperationException();
    }

}
