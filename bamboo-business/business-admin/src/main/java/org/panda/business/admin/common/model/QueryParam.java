package org.panda.business.admin.common.model;

import com.github.pagehelper.PageHelper;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页参数基类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/10
 **/
@Setter
@Getter
public class QueryParam {

    private int pageNum = 1;

    private int pageSize = 10;

    public QueryParam(){}

    public QueryParam(int pageNum, int pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public void initPage() {
        PageHelper.startPage(this.getPageNum(),this.getPageSize());
    }
}
