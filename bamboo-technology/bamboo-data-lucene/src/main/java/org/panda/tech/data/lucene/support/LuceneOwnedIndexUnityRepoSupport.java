package org.panda.tech.data.lucene.support;

import org.panda.tech.data.model.entity.unity.OwnedIndexUnity;

import java.io.Serializable;

/**
 * 基于Lucene的从属索引单体数据仓库支持
 *
 * @param <T> 索引单体类型
 * @param <K> 标识类型
 * @param <O> 所属者类型
 */
public abstract class LuceneOwnedIndexUnityRepoSupport<T extends OwnedIndexUnity<K, O>, K extends Serializable, O extends Serializable>
        extends LuceneOwnedIndexRepoSupport<T, O> {

    @Override
    protected final String getKeyPropertyName() {
        return "id";
    }

    @Override
    protected final String getDefaultPropertyName() {
        return "content";
    }

}
