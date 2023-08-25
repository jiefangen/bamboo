package org.panda.tech.data.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.panda.tech.data.model.query.QueryResult;

import java.util.List;

/**
 * 查询分页结果助手
 *
 * @author fangen
 **/
public class QueryPageHelper {
    /**
     * mybatisPlus分页结果转自定义分页组件
     *
     * @param records 自定义结果数据集
     * @param page mybatisPlus分页结果
     * @return 自定义分页结果
     * @param <T> 结果数据类型
     */
    public static <T> QueryResult<T> convertQueryResult(List<T> records, IPage page) {
        QueryResult<T> queryResult = QueryResult.of(records, (int)page.getSize(), (int)page.getCurrent(), page.getTotal());
        return queryResult;
    }

    /**
     * mybatisPlus分页结果转自定义分页组件
     *
     * @param page mybatisPlus分页结果
     * @return 自定义分页结果
     * @param <T> 结果数据类型
     */
    public static <T> QueryResult<T> convertQueryResult(IPage page) {
        QueryResult<T> queryResult = QueryResult.of(page.getRecords(), (int)page.getSize(), (int)page.getCurrent(), page.getTotal());
        return queryResult;
    }
}
