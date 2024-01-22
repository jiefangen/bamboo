package org.panda.tech.data.lucene.search;

import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.exception.ExceptionUtil;
import org.panda.bamboo.common.util.date.TemporalUtil;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Map;

/**
 * 默认的查询构建器
 */
public class DefaultQueryBuilder {

    private BooleanQuery.Builder delegate;

    public DefaultQueryBuilder() {
        this.delegate = new BooleanQuery.Builder();
    }

    public DefaultQueryBuilder add(Query query, BooleanClause.Occur occur) {
        if (query != null) {
            this.delegate.add(query, occur);
        }
        return this;
    }

    public DefaultQueryBuilder add(String name, Object value, BooleanClause.Occur occur) {
        return add(build(name, value), occur);
    }

    /**
     * 添加必须条件，类似AND
     *
     * @param query 条件
     * @return 当前构建器
     */
    public DefaultQueryBuilder must(Query query) {
        return add(query, BooleanClause.Occur.MUST);
    }

    /**
     * 添加必须条件，类似AND
     *
     * @param name  条件字段名
     * @param value 条件字段值
     * @return 当前构建器
     */
    public DefaultQueryBuilder must(String name, Object value) {
        return must(build(name, value));
    }

    /**
     * 添加必须不条件，类似AND NOT
     *
     * @param query 条件
     * @return 当前构建器
     */
    public DefaultQueryBuilder mustNot(Query query) {
        return add(query, BooleanClause.Occur.MUST_NOT);
    }

    /**
     * 添加必须不条件，类似AND NOT
     *
     * @param name  条件字段名
     * @param value 条件字段值
     * @return 当前构建器
     */
    public DefaultQueryBuilder mustNot(String name, Object value) {
        return mustNot(build(name, value));
    }

    /**
     * 添加应该条件，类似OR
     *
     * @param query 条件
     * @return 当前构建器
     */
    public DefaultQueryBuilder should(Query query) {
        return add(query, BooleanClause.Occur.SHOULD);
    }

    /**
     * 添加应该条件，类似OR
     *
     * @param name  条件字段名
     * @param value 条件字段值
     * @return 当前构建器
     */
    public DefaultQueryBuilder should(String name, Object value) {
        return should(build(name, value));
    }

    public BooleanQuery build() {
        return this.delegate.build();
    }


    /**
     * 创建默认查询条件，对于不分词字段生成精确匹配查询条件，对于分词字段生成包含匹配查询条件
     *
     * @param name  条件字段名
     * @param value 条件字段值
     * @return 默认查询条件
     */
    public static Query build(String name, Object value) {
        if (value instanceof Long) {
            return LongPoint.newExactQuery(name, (Long) value);
        }
        if (value instanceof Integer) {
            return IntPoint.newExactQuery(name, (Integer) value);
        }
        if (value instanceof BigDecimal) {
            return DoublePoint.newExactQuery(name, ((BigDecimal) value).doubleValue());
        }
        if (value instanceof Double) {
            return DoublePoint.newExactQuery(name, (Double) value);
        }
        if (value instanceof Float) {
            return FloatPoint.newExactQuery(name, (Float) value);
        }
        if (value instanceof Temporal) {
            value = TemporalUtil.format((Temporal) value);
        }
        return new TermQuery(new Term(name, value.toString()));
    }

    public static Query parse(QueryParser queryParser, String ql) {
        try {
            // 逻辑运算符大写化，以符合Lucene查询语句规范
            ql = ql.replaceAll(" and ", " AND ").replaceAll(" or ", " OR ");
            return queryParser.parse(ql);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static Query parse(QueryParser queryParser, CharSequence ql, Map<String, Object> params) {
        String s = ql.toString();
        if (params != null && params.size() > 0) {
            String[] follows = { Strings.SPACE, Strings.SINGLE_QUOTES, Strings.DOUBLE_QUOTES, "\\)" };
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    // 去掉参数值中的空格，避免条件字段错误
                    value = value.toString().replaceAll(Strings.SPACE, Strings.EMPTY);
                    String name = entry.getKey();
                    String key = Strings.COLON + name;
                    for (String follow : follows) {
                        s = s.replaceAll(key + follow, value + follow);
                    }
                    if (s.endsWith(key)) {
                        s = s.substring(0, s.length() - key.length()) + value;
                    }
                }
            }
        }
        return parse(queryParser, s);
    }

}
