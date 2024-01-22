package org.panda.tech.data.lucene.support;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;
import org.panda.bamboo.common.util.lang.ArrayUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.data.index.IndexRepo;
import org.panda.tech.data.lucene.document.IndexFieldFeature;
import org.panda.tech.data.lucene.search.DefaultQueryBuilder;
import org.panda.tech.data.lucene.search.DefaultSortBuilder;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.Paged;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.Paging;
import org.panda.tech.data.model.query.spec.QueryIgnoring;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于Lucene的索引数据仓库支持
 *
 * @param <T> 索引对象类型
 */
public abstract class LuceneIndexRepoSupport<T> implements IndexRepo<T> {
    /**
     * 分词字段名后缀
     */
    protected static final String TOKENIZED_FIELD_NAME_SUFFIX = "_text";

    private Map<String, Class<?>> propertyTypes = ClassUtil.getPropertyTypes(getIndexType());

    protected Class<T> getIndexType() {
        return ClassUtil.getActualGenericType(getClass(), IndexRepo.class, 0);
    }

    /**
     * 获取标识属性名。被索引对象必须具有标识属性，否则无法定位单个对象进行删除
     *
     * @return 标识属性名
     */
    protected abstract String getKeyPropertyName();

    /**
     * 获取默认的索引属性名称。被索引对象必然有至少一个索引属性，否则整个对象都无需索引
     *
     * @return 默认的索引属性名称
     */
    protected abstract String getDefaultPropertyName();

    /**
     * 被索引对象转换为索引文档。<br>
     * 注意：必须包含标识属性同名索引字段，否则无法正确执行删除操作<br>
     * 提示：精确等于比对的字段添加{@link StringField}，关键字匹配的字段添加{@link TextField}
     *
     * @param object             被索引对象
     * @param excludedProperties 排除的属性名称集
     * @return 索引文档
     */
    protected Document toDocument(T object, String... excludedProperties) {
        Document document = new Document();
        BeanUtil.loopProperties(object, (name, value) -> {
            if (name.equals(getKeyPropertyName())) {
                document.add(getGeneralField(name, value, true));
                document.add(getGeneralField(name, value, false));
            } else {
                getFields(name, value).forEach(document::add);
            }
        }, excludedProperties);
        return document;
    }

    /**
     * 获取指定属性的不存储的索引字段信息集
     *
     * @param name  索引字段名，不含标识属性
     * @param value 索引字段值
     * @return 索引字段信息，返回null表示当前条件下没有对应的索引字段
     */
    protected Collection<IndexableField> getFields(String name, Object value) {
        Collection<IndexableField> fields = new ArrayList<>();
        IndexFieldFeature feature = getFieldFeature(name);
        if (value != null && value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(value, i);
                addFields(fields, name, element, feature);
            }
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            for (Object element : collection) {
                addFields(fields, name, element, feature);
            }
        } else {
            addFields(fields, name, value, feature);
        }
        return fields;
    }

    /**
     * 获取指定属性的索引字段特性
     *
     * @param name 属性名
     * @return 索引字段特性，不能返回null，默认：不存储、不分词、不参与排序
     */
    protected IndexFieldFeature getFieldFeature(String name) {
        if (name.equals(getDefaultPropertyName())) { // 默认索引字段不存储、分词、不参与排序
            return new IndexFieldFeature(false, true, false);
        }
        return new IndexFieldFeature();
    }

    protected void addFields(Collection<IndexableField> fields, String name, Object value, IndexFieldFeature feature) {
        if (feature.isSorted()) {
            IndexableField sortedField = getSortedField(name, value);
            if (sortedField != null) {
                fields.add(sortedField);
            }
        }
        boolean tokenized = feature.isTokenized();
        if (tokenized) {
            IndexableField tokenizedField = getTokenizedField(name, value);
            if (tokenizedField != null) {
                fields.add(tokenizedField);
            }
        }
        // 默认索引属性一般都超长，不生成一般性的可索引字段，没有进行等于比较的需求，浪费存储空间，且容易造成超长错误
        if (!name.equals(getDefaultPropertyName())) {
            IndexableField generalField = getGeneralField(name, value, feature.isStored());
            if (generalField != null) {
                fields.add(generalField);
            }
        }
    }

    protected IndexableField getSortedField(String name, Object value) {
        if (value == null || ClassUtil.isComplex(value.getClass())) {
            return null;
        }
        if (value instanceof Number) {
            if (value instanceof Long) {
                return new NumericDocValuesField(name, (Long) value);
            }
            if (value instanceof Integer) {
                return new NumericDocValuesField(name, (Integer) value);
            }
            if (value instanceof BigDecimal) {
                return new DoubleDocValuesField(name, ((BigDecimal) value).doubleValue());
            }
            if (value instanceof Double) {
                return new DoubleDocValuesField(name, (Double) value);
            }
            if (value instanceof Float) {
                return new FloatDocValuesField(name, (Float) value);
            }
        }
        if (value instanceof Temporal) {
            value = TemporalUtil.format((Temporal) value);
        }
        return new SortedDocValuesField(name, new BytesRef(value.toString()));
    }

    /**
     * 获取指定属性的分词索引字段信息
     *
     * @param name  索引字段名
     * @param value 索引字段值
     * @return 索引字段信息，返回null表示不索引
     */
    protected IndexableField getTokenizedField(String name, Object value) {
        String text = value == null ? Strings.EMPTY : value.toString();
        // 默认情况下，分词字段附加分词字段后缀作为索引字段名，以便区分普通查询和分词查询
        return new TextField(name + TOKENIZED_FIELD_NAME_SUFFIX, text, Field.Store.NO);
    }

    /**
     * 获取指定属性的一般性索引字段信息<br>
     * 如果一个属性对应多个一般性索引字段，请子类覆写 {@link #getFields(String, Object)}
     *
     * @param name   索引字段名
     * @param value  索引字段值
     * @param stored 是否存储
     * @return 索引字段信息，返回null表示不索引
     */
    protected IndexableField getGeneralField(String name, Object value, boolean stored) {
        if (value == null || ClassUtil.isComplex(value.getClass())) {
            return null;
        }
        if (value instanceof Number) {
            if (value instanceof Long) {
                if (stored) {
                    return new StoredField(name, (Long) value);
                } else {
                    return new LongPoint(name, (Long) value);
                }
            }
            if (value instanceof Integer) {
                if (stored) {
                    return new StoredField(name, (Integer) value);
                } else {
                    return new IntPoint(name, (Integer) value);
                }
            }
            if (value instanceof BigDecimal) {
                double doubleValue = ((BigDecimal) value).doubleValue();
                if (stored) {
                    return new StoredField(name, doubleValue);
                } else {
                    return new DoublePoint(name, doubleValue);
                }
            }
            if (value instanceof Double) {
                if (stored) {
                    return new StoredField(name, (Double) value);
                } else {
                    return new DoublePoint(name, (Double) value);
                }
            }
            if (value instanceof Float) {
                if (stored) {
                    return new StoredField(name, (Float) value);
                } else {
                    return new FloatPoint(name, (Float) value);
                }
            }
        }
        if (value instanceof Temporal) {
            value = TemporalUtil.format((Temporal) value);
        }
        String s = value.toString();
        // 转换为二进制后不能超过32766 bytes，最长耗时约1.5s
        s = StringUtil.cutForBytes(s, 32766);
        return new StringField(name, s, stored ? Field.Store.YES : Field.Store.NO);
    }

    /**
     * 获取指定属性的不存储的一般性索引字段信息<br>
     * 如果一个属性对应多个一般性索引字段，请子类覆写 {@link #getFields(String, Object)}
     *
     * @param name  索引字段名
     * @param value 索引字段值
     * @return 索引字段信息，返回null表示不索引
     */
    protected IndexableField getGeneralField(String name, Object value) {
        return getGeneralField(name, value, false);
    }

    ////// 以上为索引部分，以下为检索部分 //////

    /**
     * 构建分词查询条件
     *
     * @param queryParser 查询解析器
     * @param name        索引字段名
     * @param keywords    查询关键字集
     * @return 分词查询条件
     */
    protected Query buildTokenizedQuery(QueryParser queryParser, String name, String[] keywords) {
        if (keywords.length == 1) {
            String keyword = keywords[0].trim();
            return buildTokenizedQuery(queryParser, name, keyword);
        } else if (keywords.length > 1) {
            DefaultQueryBuilder builder = new DefaultQueryBuilder();
            for (String keyword : keywords) {
                keyword = keyword.trim();
                // 空格分隔的多个关键字之间为and条件关系
                builder.must(buildTokenizedQuery(queryParser, name, keyword));
            }
            return builder.build();
        }
        return null;
    }

    protected Query buildTokenizedQuery(QueryParser queryParser, String name, String keyword) {
        // 单个关键字条件形如：[name]:[keyword] OR [name]_text:[keyword]
        // 其中分词索引字段使用查询语句解析构建，以获得分词查询能力
        keyword = keyword.replaceAll(Strings.SLASH, "\\\\/");
        DefaultQueryBuilder builder = new DefaultQueryBuilder();
        builder.should(DefaultQueryBuilder.parse(queryParser, name + ":/.*" + keyword + ".*/"));
        return builder
                .should(DefaultQueryBuilder.parse(queryParser, name + TOKENIZED_FIELD_NAME_SUFFIX + ":" + keyword))
                .build();
    }

    /**
     * 检索
     *
     * @param searcher     索引检索器
     * @param query        查询条件
     * @param pageSize     分页大小
     * @param pageNo       页码
     * @param sort         排序
     * @param ignoring     查询忽略项
     * @param fieldsToLoad 查询结果要加载的字段集，为空时不加载任何字段
     * @return 检索结果
     */
    protected final QueryResult<Document> search(IndexSearcher searcher, Query query, int pageSize, int pageNo,
                                                 Sort sort, QueryIgnoring ignoring, String... fieldsToLoad) {
        try {
            List<Document> records = null;
            Long total = null;
            ScoreDoc afterDoc = null;
            if (ignoring != QueryIgnoring.RECORD) {
                afterDoc = getAfterDoc(searcher, query, pageSize, pageNo, sort);
            }
            // 期望的最大数量，指定页大小小<=0时，不限制数量
            int expectedSize = pageSize > 0 ? pageSize : Integer.MAX_VALUE;
            // 查询的最大数量，实际多查一条记录，以便于判断是否还有更多记录
            int querySize = pageSize > 0 ? (expectedSize + 1) : Integer.MAX_VALUE;
            TopDocs topDocs = searcher.searchAfter(afterDoc, query, querySize, sort);
            if (ignoring != QueryIgnoring.TOTAL) {
                // 总数 = 之前的记录总数 + 当前命中的总数
                total = (long) pageSize * (pageNo - 1) + topDocs.totalHits.value;
            }
            if (ignoring != QueryIgnoring.RECORD) {
                records = new ArrayList<>();
                // 因为多查了一条记录，所以当前页记录数可能超过expectedSize，二者之间的最小值为实际结果记录数量
                int actualSize = Math.min(topDocs.scoreDocs.length, expectedSize);
                for (int i = 0; i < actualSize; i++) {
                    ScoreDoc scoreDoc = topDocs.scoreDocs[i];
                    Document document = searcher.doc(scoreDoc.doc, ArrayUtil.toSet(fieldsToLoad));
                    records.add(document);
                }
            }
            Paged paged;
            if (total == null) {
                // 多查一条的结果记录数量如果大于期望数量，说明还有更多记录
                paged = new Paged(pageSize, pageNo, topDocs.scoreDocs.length > expectedSize);
            } else {
                paged = new Paged(pageSize, pageNo, total);
            }
            return new QueryResult<>(records, paged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScoreDoc getAfterDoc(IndexSearcher searcher, Query query, int pageSize, int pageNo, Sort sort)
            throws IOException {
        int n = pageSize * (pageNo - 1);
        if (n > 0) {
            TopDocs topDocs = searcher.search(query, n, sort);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            return scoreDocs[scoreDocs.length - 1];
        }
        return null;
    }

    /**
     * 检索
     *
     * @param searcher     索引检索器
     * @param query        查询条件
     * @param paging       分页信息
     * @param fieldsToLoad 查询结果要加载的字段集，为空时不加载任何字段
     * @return 检索结果
     */
    protected final QueryResult<Document> search(IndexSearcher searcher, Query query, Paging paging,
                                                 String... fieldsToLoad) {
        Sort sort = buildSort(paging.getOrders());
        return search(searcher, query, paging.getPageSize(), paging.getPageNo(), sort, paging.getIgnoring(),
                fieldsToLoad);
    }

    /**
     * 构建索引排序
     *
     * @param orders 字段排序集
     * @return 索引排序
     */
    protected Sort buildSort(List<FieldOrder> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        DefaultSortBuilder builder = new DefaultSortBuilder();
        for (FieldOrder order : orders) {
            String fieldName = order.getName();
            Class<?> fieldType = this.propertyTypes.get(fieldName);
            SortField sortField = buildSortField(fieldName, fieldType, order.isDesc());
            builder.add(sortField);
        }
        return builder.build();
    }

    /**
     * 构建索引排序字段
     *
     * @param fieldName 排序字段名
     * @param fieldType 推断的字段类型，无法推断时为null
     * @param desc      是否倒序
     * @return 索引排序字段
     */
    protected SortField buildSortField(String fieldName, Class<?> fieldType, boolean desc) {
        SortField.Type sortFieldType = getSortFieldType(fieldName, fieldType);
        return new SortField(fieldName, sortFieldType, desc);
    }

    /**
     * 获取排序字段类型
     *
     * @param fieldName 排序字段名
     * @param fieldType 推断的字段类型，无法推断时为null
     * @return 排序字段类型
     */
    protected SortField.Type getSortFieldType(String fieldName, Class<?> fieldType) {
        IndexFieldFeature feature = getFieldFeature(fieldName);
        if (fieldType != null) { // 数字类型一定可排序
            if (fieldType == Long.class || fieldType == long.class) {
                return SortField.Type.LONG;
            }
            if (fieldType == Integer.class || fieldType == int.class) {
                return SortField.Type.INT;
            }
            if (fieldType == BigDecimal.class || fieldType == Double.class || fieldType == double.class) {
                return SortField.Type.DOUBLE;
            }
            if (fieldType == Float.class || fieldType == float.class) {
                return SortField.Type.FLOAT;
            }
        }
        if (feature.isSorted()) {
            // 分词字段 -> 计分排序
            if (feature.isTokenized()) {
                return SortField.Type.SCORE;
            }
            // 默认使用字符串顺序
            return SortField.Type.STRING;
        }
        // 不参与排序则使用文档顺序
        return SortField.Type.DOC;
    }

    protected final void clear(IndexWriter writer) {
        Directory directory = writer.getDirectory();
        try {
            String[] names = directory.listAll();
            for (String name : names) {
                if (!"write.lock".equals(name)) {
                    directory.deleteFile(name);
                }
            }
            writer.close();
        } catch (IOException e) {
            LogUtil.error(getClass(), e);
        }
    }

}
