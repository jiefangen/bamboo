package org.panda.common.domain;

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

    private int pageNo = 1;

    private int pageSize;

    public QueryParam(){}

    public QueryParam(int pageNo, int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public void initPage(){
        PageHelper.startPage(this.getPageNo(),this.getPageSize());
    }
}
