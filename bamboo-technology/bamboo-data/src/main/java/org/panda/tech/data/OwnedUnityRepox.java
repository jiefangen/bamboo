package org.panda.tech.data;

import org.panda.tech.data.model.entity.unity.OwnedUnity;

import java.io.Serializable;

/**
 * 从属单体的数据访问仓库扩展
 *
 * @param <T> 单体类型
 * @param <K> 标识类型
 * @param <O> 所属者类型
 */
public interface OwnedUnityRepox<T extends OwnedUnity<K, O>, K extends Serializable, O extends Serializable>
        extends UnityRepox<T, K> {
    /**
     * 获取指定所属者下的单体个数
     *
     * @param owner 所属者
     * @return 指定所属者下的单体个数
     */
    long countByOwner(O owner);

    /**
     * 获取指定所属者下指定标识的单体
     *
     * @param owner 所属者
     * @param id    单体标识
     * @return 单体
     */
    T findByOwnerAndId(O owner, K id);

    /**
     * 递增指定单体的指定数值属性值
     *
     * @param owner        所属者
     * @param id           单体标识
     * @param propertyName 数值属性名
     * @param step         递增的值，为负值即表示递减
     * @param limit        增减后允许的最大/最小值，设定以避免数值超限
     * @param <N>          数值类型
     * @return 单体
     */
    <N extends Number> T increaseNumber(O owner, K id, String propertyName, N step, N limit);

}
