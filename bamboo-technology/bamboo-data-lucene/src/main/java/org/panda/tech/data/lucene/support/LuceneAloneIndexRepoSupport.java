package org.panda.tech.data.lucene.support;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.tech.data.index.AloneIndexRepo;
import org.panda.tech.data.lucene.index.IndexFactory;
import org.panda.tech.data.lucene.search.DefaultQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * 基于Lucene的独立索引数据仓库支持
 *
 * @param <T> 独立索引对象类型
 */
public abstract class LuceneAloneIndexRepoSupport<T> extends LuceneIndexRepoSupport<T> implements AloneIndexRepo<T> {

    @Autowired
    private IndexFactory indexFactory;

    /**
     * @return 当前仓库相对于存储根目录的路径
     */
    protected abstract String getDirectoryPath();

    @Override
    public long getSpaceSize() {
        return this.indexFactory.getSpaceSize(getDirectoryPath());
    }

    protected IndexWriter getWriter() {
        try {
            return this.indexFactory.getWriter(getDirectoryPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected QueryParser getQueryParser() {
        try {
            return this.indexFactory.getQueryParser(getDirectoryPath(), getDefaultPropertyName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected IndexSearcher getSearcher() {
        try {
            return this.indexFactory.getSearcher(getDirectoryPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(T object) {
        Document document = toDocument(object);
        try {
            if (document != null && document.iterator().hasNext()) {
                getWriter().addDocument(document);
            }
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void delete(T object) {
        String propertyName = getKeyPropertyName();
        Object propertyValue = BeanUtil.getPropertyValue(object, propertyName);
        Query query = DefaultQueryBuilder.build(propertyName, propertyValue);
        try {
            getWriter().deleteDocuments(query);
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
    public boolean isSearchable() {
        try {
            return DirectoryReader.indexExists(getWriter().getDirectory());
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
            return false;
        }
    }

    protected final Query parse(String ql) {
        return DefaultQueryBuilder.parse(getQueryParser(), ql);
    }

    protected final Query parse(CharSequence ql, Map<String, Object> params) {
        return DefaultQueryBuilder.parse(getQueryParser(), ql, params);
    }

    @Override
    public void commit() {
        try {
            IndexWriter writer = getWriter();
            writer.forceMergeDeletes();
            writer.commit();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void rollback() {
        try {
            getWriter().rollback();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

    @Override
    public void clear() {
        clear(getWriter());
    }

}
