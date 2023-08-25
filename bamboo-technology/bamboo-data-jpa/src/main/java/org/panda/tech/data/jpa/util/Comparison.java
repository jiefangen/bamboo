package org.panda.tech.data.jpa.util;

/**
 * 比较操作符
 */
public enum Comparison {
    /**
     * 等于
     */
    EQUAL(false),
    /**
     * 不等于
     */
    NOT_EQUAL(true),
    /**
     * 两边都有%的like
     */
    LIKE(false),
    /**
     * 两边都有%的not like
     */
    NOT_LIKE(true),
    /**
     * %在左边的like
     */
    LIKE_LEFT(false),
    /**
     * %在左边的not like
     */
    NOT_LIKE_LEFT(true),
    /**
     * %在右边的like
     */
    LIKE_RIGHT(false),
    /**
     * %在右边的not like
     */
    NOT_LIKE_RIGHT(true),
    /**
     * in
     */
    IN(false),
    /**
     * not in
     */
    NOT_IN(true),
    /**
     * 大于
     */
    GREATER(false),
    /**
     * 大于等于
     */
    GREATER_EQUAL(false),
    /**
     * 小于
     */
    LESS(false),
    /**
     * 小于等于
     */
    LESS_EQUAL(false),
    /**
     * 为空
     */
    IS_NULL(false),
    /**
     * 不为空
     */
    NOT_NULL(true);

    private Comparison(boolean not) {
        this.not = not;
    }

    private boolean not;

    public boolean isNot() {
        return this.not;
    }

    /**
     * @return 是否一元比较符
     */
    public boolean isUnary() {
        return this == IS_NULL || this == NOT_NULL;
    }

    /**
     * @return 是否多元比较符
     */
    public boolean isMultiple() {
        return this == IN || this == NOT_IN;
    }

    /**
     * 获取在查询语言中的字符串形式.
     *
     * @return 在查询语言中的字符串形式
     */
    public String toQlString() {
        switch (this) {
            case NOT_EQUAL:
                return " <> ";
            case LIKE:
            case LIKE_LEFT:
            case LIKE_RIGHT:
                return " like ";
            case NOT_LIKE:
            case NOT_LIKE_LEFT:
            case NOT_LIKE_RIGHT:
                return " not like ";
            case IN:
                return " in ";
            case NOT_IN:
                return " not in ";
            case GREATER:
                return " > ";
            case GREATER_EQUAL:
                return " >= ";
            case LESS:
                return " < ";
            case LESS_EQUAL:
                return " <= ";
            case IS_NULL:
                return " is null";
            case NOT_NULL:
                return " is not null";
            default:
                return " = ";
        }
    }
}
