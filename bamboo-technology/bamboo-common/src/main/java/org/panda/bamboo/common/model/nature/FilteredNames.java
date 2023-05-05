package org.panda.bamboo.common.model.nature;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 过滤名称集
 */
public class FilteredNames {

    private Set<String> includes;
    private Set<String> excludes;

    public Set<String> getIncludes() {
        return this.includes;
    }

    public Set<String> getExcludes() {
        return this.excludes;
    }

    /**
     * 添加包含的符号集
     *
     * @param names 包含的名称集
     */
    public void addIncluded(String... names) {
        if (this.includes == null) {
            this.includes = new HashSet<>();
        }
        for (String property : names) {
            this.includes.add(property);
        }
    }

    /**
     * 添加排除的符号集
     *
     * @param names 排除的名称集
     */
    public void addExcluded(String... names) {
        if (this.excludes == null) {
            this.excludes = new HashSet<>();
        }
        for (String property : names) {
            this.excludes.add(property);
        }
    }

    /**
     * @return 包含符号集和排除符号集是否均为空
     */
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.includes) && CollectionUtils.isEmpty(this.excludes);
    }

    public boolean include(String name) {
        // 限定了包含范围，但不在包含范围内，则不匹配
        if (!CollectionUtils.isEmpty(this.includes) && !this.includes.contains(name)) {
            return false;
        }
        // 限定了排除范围，且在排除范围内，则不匹配
        if (!CollectionUtils.isEmpty(this.excludes) && this.excludes.contains(name)) {
            return false;
        }
        // 其它情况均为匹配
        return true;
    }

}
