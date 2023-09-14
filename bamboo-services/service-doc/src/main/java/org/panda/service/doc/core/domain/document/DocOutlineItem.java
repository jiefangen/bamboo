package org.panda.service.doc.core.domain.document;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档纲要条目
 */
public class DocOutlineItem {

    private int level;
    private String caption;
    private int pageIndex = -1;
    private List<DocOutlineItem> subs;

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<DocOutlineItem> getSubs() {
        return this.subs;
    }

    public void setSubs(List<DocOutlineItem> subs) {
        this.subs = subs;
    }

    public void addSub(DocOutlineItem sub) {
        if (this.subs == null) {
            this.subs = new ArrayList<>();
        }
        this.subs.add(sub);
    }

}
