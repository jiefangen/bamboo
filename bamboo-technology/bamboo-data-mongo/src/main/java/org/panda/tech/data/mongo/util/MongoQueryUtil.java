package org.panda.tech.data.mongo.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * MongoDB查询工具类
 */
public class MongoQueryUtil {

    private MongoQueryUtil() {
    }

    /**
     * 用指定Criteria集合以and的形式构建一个Query对象
     *
     * @param criteriaList Criteria集合
     * @return Query对象
     */
    public static Query buildQuery(List<Criteria> criteriaList) {
        if (criteriaList == null || criteriaList.isEmpty()) {
            return new Query(new Criteria());
        }
        Criteria[] array = criteriaList.toArray(new Criteria[0]);
        return new Query(new Criteria().andOperator(array));
    }

    public static Query buildOrQuery(List<Criteria> criteriaList) {
        Criteria[] array = criteriaList.toArray(new Criteria[0]);
        return new Query(new Criteria().orOperator(array));
    }

}
