package org.panda.tech.data.lucene.search;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的排序构建器
 */
public class DefaultSortBuilder {

    private List<SortField> fields = new ArrayList<>();

    public DefaultSortBuilder add(SortField sortField) {
        this.fields.add(sortField);
        return this;
    }

    public DefaultSortBuilder add(String field, SortField.Type type, boolean reverse) {
        return this.add(new SortField(field, type, reverse));
    }

    public Sort build() {
        return new Sort(this.fields.toArray(new SortField[0]));
    }

}
