package org.panda.tech.data.model.entity.unity;

import org.panda.tech.data.model.entity.Entity;

import java.io.Serializable;

/**
 * 单体，用id作为标识属性的实体
 *
 * @param <K> 标识类型
 */
public interface Unity<K extends Serializable> extends Entity {
    /**
     * 获取标识
     *
     * @return 标识，唯一表示一个单体
     */
    K getId();
}
