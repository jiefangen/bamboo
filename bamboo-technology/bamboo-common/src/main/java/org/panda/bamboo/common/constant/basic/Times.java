package org.panda.bamboo.common.constant.basic;

/**
 * 时间格式常量集
 **/
public class Times {

    private Times() {
    }

    /**
     * 短日期格式
     */
    public static final String SHORT_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    public static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * 时间格式（12小时制）
     */
    public static final String TIME_PATTERN_12HOURS = "ahh:mm:ss";
    /**
     * 精确到分钟的时间格式
     */
    public static final String TIME_PATTERN_TO_MINUTE = "HH:mm";
    /**
     * 精确到分钟的时间格式（12小时制）
     */
    public static final String TIME_PATTERN_12HOURS_TO_MINUTE = "ahh:mm";
    /**
     * 长日期格式
     */
    public static final String LONG_DATE_PATTERN = SHORT_DATE_PATTERN + Strings.SPACE + TIME_PATTERN;
    /**
     * 没分隔符长日期格式
     */
    public static final String LONG_DATE_NO_DELIMITER_PATTERN = "yyyyMMddHHmmss";
    /**
     * 没分隔符的精确到毫秒的时间戳格式
     */
    public static final String LONG_TIMESTAMP_NO_DELIMITER_PATTERN = LONG_DATE_NO_DELIMITER_PATTERN + "S";
    /**
     * 精确到分钟的长日期格式
     */
    public static final String LONG_DATE_PATTERN_TO_MINUTE = "yyyy-MM-dd HH:mm";
    /**
     * 格林威治标准时间格式
     */
    public static final String GMT_PATTERN = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

    public static final long MS_ONE_SECOND = 1000;

    public static final long MS_ONE_MINUTE = 60 * MS_ONE_SECOND;

    public static final long MS_ONE_HOUR = 60 * MS_ONE_MINUTE;

    public static final long MS_ONE_DAY = 24 * MS_ONE_HOUR;
}
