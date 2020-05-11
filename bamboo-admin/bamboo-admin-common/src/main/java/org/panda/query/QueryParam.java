package org.panda.query;

/**
 * 分页参数基类
 *
 * @author jvfagan
 * @since JDK 1.8  2020/5/10
 **/
public class QueryParam {

    private int pageNo = 1;

    private int pageSize;

    public QueryParam(){
    }

    public QueryParam(int pageNo, int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
