package org.panda.tech.data.jpa.util;

import org.panda.tech.data.model.query.QueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 查询分页结果助手
 *
 * @author fangen
 **/
public class QueryPageHelper {
    /**
     * JPA分页结果转自定义分页组件
     *
     * @param records 自定义结果数据集
     * @param pageable JPA分页结果
     * @return 自定义分页结果
     * @param <T> 结果数据类型
     */
    public static <T> QueryResult<T> convertQueryResult(List<T> records, Pageable pageable) {
        QueryResult<T> queryResult = QueryResult.of(records, pageable.getPageSize(), pageable.getPageNumber(), (long) records.size());
        return queryResult;
    }

    /**
     * JPA分页结果转自定义分页组件
     *
     * @param page JPA分页结果
     * @return 自定义分页结果
     * @param <T> 结果数据类型
     */
    public static <T> QueryResult<T> convertQueryResult(Page<T> page) {
        QueryResult<T> queryResult = QueryResult.of(page.getContent(), page.getSize(), page.getNumber(), page.getTotalElements());
        return queryResult;
    }
}
