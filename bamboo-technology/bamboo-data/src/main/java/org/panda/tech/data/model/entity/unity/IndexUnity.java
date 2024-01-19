package org.panda.tech.data.model.entity.unity;

import java.io.Serializable;

/**
 * 索引单体
 *
 * @param <K> 标识类型
 */
public interface IndexUnity<K extends Serializable> extends Unity<K> {

    /**
     * 获取内容，内容作为默认索引属性，一般为大文本
     *
     * @return 内容
     */
    String getContent();

}
