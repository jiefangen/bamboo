package org.panda.bamboo.common.util.date;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Times;
import org.panda.bamboo.common.util.lang.MathUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * Java8新版时间相关类的工具类
 *
 * @author fangen
 */
public class TemporalUtil {

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private TemporalUtil() {
    }

    public static Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(toInstant(date));
    }

    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(toInstant(dateTime));
    }

    public static Instant toInstant(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay(DEFAULT_ZONE_ID).toInstant();
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(DEFAULT_ZONE_ID).toInstant();
    }

    public static LocalDate toLocalDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalTime toLocalTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(DEFAULT_ZONE_ID).toLocalTime();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }

    public static LocalDateTime toLocalDateTime(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            return null;
        }
        return LocalDateTime.of(date, time);
    }

    /**
     * 按照指定格式格式化时间点对象为字符串型日期
     *
     * @param temporal 时间点
     * @param pattern  日期格式
     * @return 字符串型日期
     */
    public static String format(Temporal temporal, String pattern) {
        if (temporal == null) {
            return null;
        }
        return formatter(pattern).format(temporal);
    }

    public static DateTimeFormatter formatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(DEFAULT_ZONE_ID);
    }

    public static String format(Temporal temporal) {
        if (temporal == null) {
            return null;
        } else if (temporal instanceof Instant) {
            return formatLong((Instant) temporal);
        } else if (temporal instanceof LocalDateTime) {
            return format(temporal, Times.LONG_DATE_PATTERN);
        } else if (temporal instanceof LocalDate) {
            return format(temporal, Times.SHORT_DATE_PATTERN);
        } else if (temporal instanceof LocalTime) {
            return format(temporal, Times.TIME_PATTERN);
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).format(DateTimeFormatter.ofPattern(Times.LONG_DATE_PATTERN));
        }
        return temporal.toString();
    }

    /**
     * 按照短日期格式(yyyy-MM-dd)格式化时间点对象为字符串型日期
     *
     * @param instant 时间点
     * @return 字符串型日期
     */
    public static String formatShort(Instant instant) {
        return format(instant, Times.SHORT_DATE_PATTERN);
    }

    public static String format(LocalDate date) {
        return format(date, Times.SHORT_DATE_PATTERN);
    }

    public static String format(LocalTime time) {
        return format(time, Times.TIME_PATTERN);
    }

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, Times.LONG_DATE_PATTERN);
    }

    /**
     * 按照长日期格式(yyyy-MM-dd HH:mm:ss)转换时间点对象为字符串型日期
     *
     * @param instant 时间点
     * @return 字符串型日期
     */
    public static String formatLong(Instant instant) {
        return format(instant, Times.LONG_DATE_PATTERN);
    }

    /**
     * 按照长日期格式(yyyyMMddHHmmss)转换时间点对象为字符串型日期
     *
     * @param instant 时间点
     * @return 字符串型日期
     */
    public static String formatLongNoDelimiter(Instant instant) {
        return format(instant, Times.LONG_DATE_NO_DELIMITER_PATTERN);
    }

    /**
     * 计算指定两个时间之间的相差天数。如果earlierTime晚于laterTime，则返回负值
     *
     * @param earlierTime 较早时间
     * @param laterTime   较晚时间
     * @return 相差天数
     */
    public static int daysBetween(Instant earlierTime, Instant laterTime) {
        LocalDate earlierDate = toLocalDate(earlierTime);
        LocalDate laterDate = toLocalDate(laterTime);
        return daysBetween(earlierDate, laterDate);
    }

    /**
     * 计算指定两个日期之间的相差天数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早日期
     * @param laterDate   较晚日期
     * @return 相差天数
     */
    public static int daysBetween(LocalDate earlierDate, LocalDate laterDate) {
        return (int) (laterDate.toEpochDay() - earlierDate.toEpochDay());
    }

    /**
     * 计算指定两个日期之间的相差月数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早日期
     * @param laterDate   较晚日期
     * @return 相差月数
     */
    public static int monthsBetween(LocalDate earlierDate, LocalDate laterDate) {
        int dYear = laterDate.getYear() - earlierDate.getYear();
        int dMonth = laterDate.getMonthValue() - earlierDate.getMonthValue();
        int dDay = laterDate.getDayOfMonth() - earlierDate.getDayOfMonth();
        int months = dYear * 12 + dMonth;
        if (dDay < 0) {
            months--;
        }
        return months;
    }

    public static Instant addYears(Instant instant, int years) {
        return toInstant(toLocalDateTime(instant).plusYears(years));
    }

    public static Instant addMonths(Instant instant, int months) {
        return toInstant(toLocalDateTime(instant).plusMonths(months));
    }

    public static Instant addDays(Instant instant, int days) {
        return toInstant(toLocalDateTime(instant).plusDays(days));
    }

    public static LocalDateTime addHours(LocalDateTime localDateTime, int hours) {
        return localDateTime.plusHours(hours);
    }

    public static LocalDateTime addMinutes(LocalDateTime localDateTime, int minutes) {
        return localDateTime.plusMinutes(minutes);
    }

    public static LocalDateTime addSeconds(LocalDateTime localDateTime, int seconds) {
        return localDateTime.plusSeconds(seconds);
    }

    /**
     * 为指定时间点设置时分秒纳秒，返回新日期
     *
     * @param instant      原时间点
     * @param hour         时
     * @param minute       分
     * @param second       秒
     * @param nanoOfSecond 纳秒
     * @return 新时间点
     */
    public static Instant setTime(Instant instant, int hour, int minute, int second, int nanoOfSecond) {
        LocalDateTime dateTime = toLocalDateTime(instant);
        dateTime = setTime(dateTime, hour, minute, second, nanoOfSecond);
        return toInstant(dateTime);
    }

    /**
     * 为指定时间点设置时分秒纳秒，返回新日期
     *
     * @param dateTime     原时间
     * @param hour         时
     * @param minute       分
     * @param second       秒
     * @param nanoOfSecond 纳秒
     * @return 新时间
     */
    public static LocalDateTime setTime(LocalDateTime dateTime, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                hour, minute, second, nanoOfSecond);
    }

    public static Instant parseInstant(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                Long millis = MathUtil.parseLongObject(s);
                if (millis != null) { // 纯数字的视为毫秒数进行解析
                    return Instant.ofEpochMilli(millis);
                }
                return formatter(Times.LONG_DATE_PATTERN).parse(s, Instant::from);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static LocalDate parseDate(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                return formatter(Times.SHORT_DATE_PATTERN).parse(s, LocalDate::from);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static LocalTime parseTime(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                return formatter(Times.TIME_PATTERN).parse(s, LocalTime::from);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static LocalDateTime parseDateTime(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                return formatter(Times.LONG_DATE_PATTERN).parse(s, LocalDateTime::from);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static <T extends Temporal> T parse(Class<T> type, String s) {
        if (type == Instant.class) {
            return (T) parseInstant(s);
        } else if (type == LocalDate.class) {
            return (T) parseDate(s);
        } else if (type == LocalDateTime.class) {
            return (T) parseDateTime(s);
        } else if (type == LocalTime.class) {
            return (T) parseTime(s);
        }
        return null;
    }

    public static <T extends Temporal> T parse(Class<T> type, String s, String pattern) {
        if (StringUtils.isNotBlank(s)) {
            try {
                if (type == Instant.class) {
                    return (T) formatter(pattern).parse(s, Instant::from);
                } else if (type == LocalDate.class) {
                    return (T) formatter(pattern).parse(s, LocalDate::from);
                } else if (type == LocalDateTime.class) {
                    return (T) formatter(pattern).parse(s, LocalDateTime::from);
                } else if (type == LocalTime.class) {
                    return (T) formatter(pattern).parse(s, LocalTime::from);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}
