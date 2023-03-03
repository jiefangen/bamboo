package org.panda.data.model.result;

import org.panda.data.model.Pagination;

/**
 * 分页结果
 *
 * @author fangen
 */
public class PagedResult extends Pagination {
    private static final long serialVersionUID = 8986543678997992553L;

    private Long total;
    private boolean morePage;

    protected PagedResult() {
    }

    public PagedResult(int pageSize, int currentPage, long totalPages) {
        super(pageSize, currentPage);
        this.total = totalPages;
        this.morePage = ((long) pageSize * currentPage) < totalPages;
    }

    public PagedResult(int pageSize, int pageNo, boolean morePage) {
        super(pageSize, pageNo);
        this.morePage = morePage;
    }

    public static PagedResult of(Pagination pagination, long totalPages) {
        PagedResult pagedResult = new PagedResult(pagination.getPageSize(), pagination.getPageNo(), totalPages);
        return pagedResult;
    }

    public static PagedResult of(Pagination pagination, boolean morePage) {
        PagedResult pagedResult = new PagedResult(pagination.getPageSize(), pagination.getPageNo(), morePage);
        return pagedResult;
    }

    public Long getTotalPages() {
        return this.total;
    }

    public boolean isMorePage() {
        return this.morePage;
    }

    public boolean isCountable() {
        return this.total != null && this.total >= 0;
    }

    public Integer getPageCount() {
        if (isPageable() && isCountable()) {
            int pageSize = getPageSize();
            int pageCount = (int) (this.total / pageSize);
            if (this.total % pageSize != 0) {
                pageCount++;
            }
            return pageCount;
        }
        return null;
    }

    public int getPreviousPage() {
        int pageNo = getPageNo();
        return pageNo <= 1 ? 1 : (pageNo - 1);
    }

    public Integer getNextPage() {
        Integer pageCount = getPageCount();
        if (pageCount == null) {
            return null;
        }
        int pageNo = getPageNo();
        return pageNo >= pageCount ? pageCount : (pageNo + 1);
    }

}
