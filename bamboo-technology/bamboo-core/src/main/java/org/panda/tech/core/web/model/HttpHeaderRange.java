package org.panda.tech.core.web.model;

import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.lang.MathUtil;

public class HttpHeaderRange {

    private String value;
    private Long beginIndex;
    private Long endIndex;

    public HttpHeaderRange(String value) {
        this.value = value.trim();
        int index = this.value.indexOf(Strings.MINUS);
        if (index >= 0) {
            this.beginIndex = MathUtil.parseLongObject(this.value.substring(0, index));
            this.endIndex = MathUtil.parseLongObject(this.value.substring(index + 1));
        }
    }

    public String getValue() {
        return this.value;
    }

    public Long getBeginIndex() {
        return this.beginIndex;
    }

    public Long getEndIndex() {
        return this.endIndex;
    }

    public long getBeginIndex(long defaultIndex) {
        return this.beginIndex == null ? defaultIndex : this.beginIndex;
    }

    public long getEndIndex(long defaultIndex) {
        return this.endIndex == null ? defaultIndex : this.endIndex;
    }

    public long getLength(long defaultLength) {
        if (this.beginIndex != null && this.endIndex != null) {
            return this.endIndex - this.beginIndex + 1;
        }
        return defaultLength;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
