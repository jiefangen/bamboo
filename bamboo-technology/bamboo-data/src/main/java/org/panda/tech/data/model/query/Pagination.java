package org.panda.tech.data.model.query;

import lombok.Getter;

import java.io.Serializable;

/**
 * 分页组件模型
 *
 * @author fangen
 */
@Getter
public class Pagination implements Serializable {
    private static final long serialVersionUID = 9076452816013992553L;

    private int pageNo = 1;
    private int pageSize;

    public Pagination() {
        this.pageSize = 10; // 默认每页大小10
    }

    public Pagination(int pageSize, int pageNo) {
        setPageSize(pageSize);
        setCurrentPage(pageNo);
    }

    /**
     * 每页大小。最小为0，表示不分页；小于0的赋值会被强制视为0
     */
    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(pageSize, 0);
    }

    /**
     * 当前页码。从1开始计数，小于1的赋值会被强制视为1
     */
    public void setCurrentPage(int pageNo) {
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
    }

    /**
     * 如果当前页大小未设定，则设定为指定页大小默认值
     */
    public void setPageSizeDefault(int pageSize) {
        if (this.pageSize <= 0) {
            this.pageSize = pageSize;
        }
    }

    /**
     * 是否还能够分页
     */
    public boolean isPageable() {
        return getPageSize() > 0;
    }

}
