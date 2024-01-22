package org.panda.tech.data.lucene.support;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.panda.bamboo.common.model.spec.Owned;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.tech.data.index.OwnedIndexRepo;
import org.panda.tech.data.lucene.index.IndexFactory;
import org.panda.tech.data.lucene.search.DefaultQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * 基于Lucene的从属索引数据仓库支持
 *
 * @param <T> 从属索引对象类型
 * @param <O> 所属者类型
 */
public abstract class LuceneOwnedIndexRepoSupport<T extends Owned<O>, O extends Serializable>
        extends LuceneIndexRepoSupport<T> implements OwnedIndexRepo<T, O> {

    @Autowired
    private IndexFactory indexFactory;

    /**
     * @param owner 所属者
     * @return 当前仓库相对于存储根目录的路径
     */
    protected abstract String getDirectoryPath(O owner);

    @Override
    public long getSpaceSize(O owner) {
        return this.indexFactory.getSpaceSize(getDirectoryPath(owner));
    }

    protected IndexWriter getWriter(O owner) {
        try {
            return this.indexFactory.getWriter(getDirectoryPath(owner));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected QueryParser getQueryParser(O owner) {
        try {
            return this.indexFactory.getQueryParser(getDirectoryPath(owner), getDefaultPropertyName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected IndexSearcher getSearcher(O owner) {
        try {
            return this.indexFactory.getSearcher(getDirectoryPath(owner));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(T object) {
        Document document = toDocument(object);
        try {
            if (document != null && document.iterator().hasNext()) {
                getWriter(object.getOwner()).addDocument(document);
            }
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    protected Document toDocument(T object, String... excludedProperties) {
        String[] properties = new String[excludedProperties.length + 1];
        if (excludedProperties.length > 0) {
            System.arraycopy(excludedProperties, 0, properties, 0, excludedProperties.length);
        }
        properties[properties.length - 1] = "owner";
        return super.toDocument(object, properties);
    }

    @Override
    public void delete(T object) {
        String propertyName = getKeyPropertyName();
        Object propertyValue = BeanUtil.getPropertyValue(object, propertyName);
        Query query = DefaultQueryBuilder.build(propertyName, propertyValue);
        try {
            getWriter(object.getOwner()).deleteDocuments(query);
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    /**
     * 判断当前索引可否检索
     *
     * @return 当前索引可否检索
     */
    @Override
    public boolean isSearchable(O owner) {
        try {
            return DirectoryReader.indexExists(getWriter(owner).getDirectory());
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
            return false;
        }
    }

    protected final Query parse(O owner, String ql) {
        return DefaultQueryBuilder.parse(getQueryParser(owner), ql);
    }

    protected final Query parse(O owner, CharSequence ql, Map<String, Object> params) {
        return DefaultQueryBuilder.parse(getQueryParser(owner), ql, params);
    }

    @Override
    public void commit(O owner) {
        try {
            IndexWriter writer = getWriter(owner);
            writer.forceMergeDeletes();
            writer.commit();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void rollback(O owner) {
        try {
            getWriter(owner).rollback();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void clear(O owner) {
        clear(getWriter(owner));
    }

}
