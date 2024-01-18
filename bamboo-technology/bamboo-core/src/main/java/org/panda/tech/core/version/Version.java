package org.panda.tech.core.version;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;

/**
 * 版本号
 */
public class Version implements Comparable<Version> {

    private static final int DEFAULT_BASE_LEVEL = 3;

    /**
     * 构建时提供的原始字符串型版本号
     */
    private String origin;
    /**
     * 基础版本号，每一级都必须为整数
     */
    private int[] bases;
    /**
     * 构建号，可以为任意字符串，也可以为空
     */
    private String build;

    public Version(String s, int baseLevel) {
        Assert.isTrue(StringUtils.isNotBlank(s), "The full version must not be blank");
        Assert.isTrue(baseLevel > 0, "base level must be greater than 0");
        this.origin = s;
        String[] array = s.split("\\.", baseLevel + 1);
        int level = Math.min(baseLevel, array.length);
        this.bases = new int[level];
        for (int i = 0; i < this.bases.length; i++) {
            this.bases[i] = Integer.parseInt(array[i]);
        }
        if (array.length > level) {
            this.build = array[array.length - 1];
        }
    }

    public Version(String s) {
        this(s, DEFAULT_BASE_LEVEL);
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getBase() {
        return toText(false);
    }

    public String getBuild() {
        return this.build;
    }

    public String toText(boolean withBuild) {
        StringBuilder sb = new StringBuilder();
        for (int base : this.bases) {
            sb.append(base).append(Strings.DOT);
        }
        if (withBuild && StringUtils.isNotBlank(this.build)) {
            sb.append(this.build);
        } else {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toText(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Version version = (Version) o;
        return Arrays.equals(this.bases, version.bases) && Objects.equals(this.build, version.build);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.build);
        result = 31 * result + Arrays.hashCode(this.bases);
        return result;
    }

    public int compareBaseTo(Version other) {
        int level = Math.min(this.bases.length, other.bases.length);
        for (int i = 0; i < level; i++) {
            if (this.bases[i] < other.bases[i]) {
                return -1;
            }
            if (this.bases[i] > other.bases[i]) {
                return 1;
            }
        }
        return Integer.compare(this.bases.length, other.bases.length);
    }

    @Override
    public int compareTo(Version other) {
        int result = compareBaseTo(other);
        if (result == 0) {
            if (StringUtils.isBlank(this.build)) {
                if (StringUtils.isNotBlank(other.build)) {
                    result = -1;
                }
            } else {
                if (StringUtils.isBlank(other.build)) {
                    result = 1;
                } else {
                    result = this.build.compareTo(other.build);
                }
            }
        }
        return result;
    }

}
