package org.panda.service.doc.core.domain.document;

import java.util.List;

/**
 * 文档纲要
 */
public class DocOutline {

    /**
     * 总页数
     */
    private int pageCount;
    private List<DocOutlineItem> items;
    /**
     * 从一级目录项开始直到最终选中目录项的索引清单
     */
    private List<Integer> selectedItemIndexes;
    private String downloadUrl;

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<DocOutlineItem> getItems() {
        return this.items;
    }

    public void setItems(List<DocOutlineItem> items) {
        this.items = items;
    }

    public List<Integer> getSelectedItemIndexes() {
        return this.selectedItemIndexes;
    }

    public void setSelectedItemIndexes(List<Integer> selectedItemIndexes) {
        this.selectedItemIndexes = selectedItemIndexes;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}
