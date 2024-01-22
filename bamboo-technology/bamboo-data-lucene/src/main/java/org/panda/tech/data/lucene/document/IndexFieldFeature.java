package org.panda.tech.data.lucene.document;

/**
 * 索引字段特性
 */
public class IndexFieldFeature {

    private boolean sorted;
    private boolean tokenized;
    private boolean stored;

    public IndexFieldFeature() {
    }

    /**
     * @param sorted    是否参与排序
     * @param tokenized 是否分词
     * @param stored    是否存储
     */
    public IndexFieldFeature(boolean sorted, boolean tokenized, boolean stored) {
        this.sorted = sorted;
        this.tokenized = tokenized;
        this.stored = stored;
    }

    public boolean isSorted() {
        return this.sorted;
    }

    public boolean isTokenized() {
        return this.tokenized;
    }

    public boolean isStored() {
        return this.stored;
    }

}
