package org.panda.tech.data.lucene.support;

import org.panda.tech.data.model.entity.unity.IndexUnity;

import java.io.Serializable;

/**
 * 基于Lucene的索引单体数据仓库支持
 *
 * @param <T> 索引单体类型
 * @param <K> 标识类型
 */
public abstract class LuceneIndexUnityRepoSupport<T extends IndexUnity<K>, K extends Serializable>
        extends LuceneAloneIndexRepoSupport<T> {

    @Override
    protected final String getKeyPropertyName() {
        return "id";
    }

    @Override
    protected final String getDefaultPropertyName() {
        return "content";
    }

}
