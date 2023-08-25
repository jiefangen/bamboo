package org.panda.tech.data.model.query;

/**
 * 分页结果
 *
 * @author fangen
 */
public class Paged extends Pagination {
    private static final long serialVersionUID = 8986543678997992553L;

    private Long total;
    private boolean morePage;

    protected Paged() {
    }

    public Paged(int pageSize, int pageNo, long total) {
        super(pageSize, pageNo);
        this.total = total;
        this.morePage = ((long) pageSize * pageNo) < total;
    }

    public Paged(int pageSize, int pageNo, boolean morePage) {
        super(pageSize, pageNo);
        this.morePage = morePage;
    }

    public static Paged of(Pagination pagination, long totalPages) {
        Paged pagedResult = new Paged(pagination.getPageSize(), pagination.getPageNo(), totalPages);
        return pagedResult;
    }

    public static Paged of(Pagination pagination, boolean morePage) {
        Paged pagedResult = new Paged(pagination.getPageSize(), pagination.getPageNo(), morePage);
        return pagedResult;
    }

    public Long getTotal() {
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
