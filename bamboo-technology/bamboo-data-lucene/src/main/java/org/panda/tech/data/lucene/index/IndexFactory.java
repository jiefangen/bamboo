package org.panda.tech.data.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.panda.bamboo.core.util.ThreadUtil;
import org.panda.tech.data.lucene.store.DirectoryFactory;
import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * 索引工厂
 */
public class IndexFactory implements DisposableBean {

    private DirectoryFactory directoryFactory;
    private Analyzer analyzer;
    private Map<String, IndexWriter> writers = new Hashtable<>();
    private Map<String, QueryParser> queryParsers = new Hashtable<>();
    private Map<String, IndexSearcher> searchers = new Hashtable<>();

    public IndexFactory(DirectoryFactory directoryFactory, Analyzer analyzer) {
        this.directoryFactory = directoryFactory;
        this.analyzer = analyzer;
    }

    public long getSpaceSize(String path) {
        return this.directoryFactory.getSpaceSize(path);
    }

    public IndexWriter getWriter(String path) throws IOException {
        IndexWriter writer = this.writers.get(path);
        if (writer == null || !writer.isOpen()) {
            Directory directory = this.directoryFactory.getDirectory(path);
            IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
            prepareConfig(config);
            try {
                writer = new IndexWriter(directory, config);
            } catch (LockObtainFailedException e) {
                ThreadUtil.sleep(100); // 如果出现锁定异常，等待100ms重试，通常情况下可恢复正常
                writer = new IndexWriter(directory, config);
            }
            this.writers.put(path, writer);
        }
        return writer;
    }

    protected void prepareConfig(IndexWriterConfig config) {
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    public QueryParser getQueryParser(String path, String fieldName) throws IOException {
        QueryParser queryParser = this.queryParsers.get(path);
        if (queryParser == null) {
            Analyzer analyzer = getWriter(path).getAnalyzer();
            queryParser = new QueryParser(fieldName, analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            this.queryParsers.put(path, queryParser);
        }
        return queryParser;
    }

    public IndexSearcher getSearcher(String path) throws IOException {
        IndexSearcher searcher = this.searchers.get(path);
        if (searcher == null) {
            // 必须用索引目录对象创建读取器，否则无法查到数据
            Directory directory = getWriter(path).getDirectory();
            if (DirectoryReader.indexExists(directory)) {
                IndexReader reader = DirectoryReader.open(directory);
                searcher = new IndexSearcher(reader);
                this.searchers.put(path, searcher);
            }
        }
        return searcher;
    }

    @Override
    public void destroy() throws Exception {
        for (IndexSearcher searcher : this.searchers.values()) {
            searcher.getIndexReader().close();
        }
        this.searchers.clear();
        this.queryParsers.clear();
        for (IndexWriter writer : this.writers.values()) {
            writer.close();
        }
        this.writers.clear();
    }

}
