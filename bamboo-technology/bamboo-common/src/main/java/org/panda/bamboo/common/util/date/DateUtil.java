package org.panda.bamboo.common.util.date;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Times;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author fangen
 */
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 获取当前日期，短日期格式yyyy-MM-dd
     *
     * @return 当前日期
     */
    public static String getCurrentDate() {
        return formatShort(new Date());
    }

    /**
     * 获取当前时间，长日期格式yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间
     */
    public static String getCurrentTime() {
        return formatLong(new Date());
    }

    /**
     * 获取当前时间，长日期格式yyyyMMddHHmmss
     *
     * @return 当前时间
     */
    public static String getCurrentTimeNoDelimiter() {
        return formatLongNoDelimiter(new Date());
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * 按照指定格式解析字符串型日期值为日期对象
     *
     * @param date    字符串型日期
     * @param pattern 日期格式
     * @return 日期对象
     */
    public static Date parse(String date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 按照指定格式格式化日期对象为字符串型日期
     *
     * @param date    日期对象
     * @param pattern 日期格式
     * @return 字符串型日期
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 按照短日期格式(yyyy-MM-dd)解析字符串型日期值为日期对象
     *
     * @param date 字符串型日期
     * @return 日期对象
     */
    public static Date parseShort(String date) {
        return parse(date, Times.SHORT_DATE_PATTERN);
    }

    /**
     * 按照短日期格式(yyyy-MM-dd)格式化日期对象为字符串型日期
     *
     * @param date 日期对象
     * @return 字符串型日期
     */
    public static String formatShort(Date date) {
        return format(date, Times.SHORT_DATE_PATTERN);
    }

    /**
     * 按照长日期格式(yyyy-MM-dd HH:mm:ss)解析字符串型日期值为日期对象
     *
     * @param date 字符串型日期
     * @return 日期对象
     */
    public static Date parseLong(String date) {
        return parse(date, Times.LONG_DATE_PATTERN);
    }

    /**
     * 按照长日期格式(yyyy-MM-dd HH:mm:ss)转换日期对象为字符串型日期
     *
     * @param date 日期对象
     * @return 字符串型日期
     */
    public static String formatLong(Date date) {
        return format(date, Times.LONG_DATE_PATTERN);
    }

    /**
     * 按照长日期格式(yyyyMMddHHmmss)转换日期对象为字符串型日期
     *
     * @param date 日期对象
     * @return 字符串型日期
     */
    public static String formatLongNoDelimiter(Date date) {
        return format(date, Times.LONG_DATE_NO_DELIMITER_PATTERN);
    }

    /**
     * 按照格林威治时间格式解析字符串型日期值为日期对象
     *
     * @param s 字符串型日期
     * @return 日期对象
     */
    public static Date parseGmt(String s) {
        DateFormat formatter = new SimpleDateFormat(Times.GMT_PATTERN, Locale.UK);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return formatter.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定时间的日历对象
     *
     * @param date 时间
     * @return 日历对象
     */
    public static Calendar getCalendar(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * 计算指定两个时间之间的相差年份数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 相差年份数
     */
    public static int yearsBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = Calendar.getInstance();
        Calendar laterCalendar = Calendar.getInstance();
        earlierCalendar.setTime(earlierDate);
        laterCalendar.setTime(laterDate);
        return laterCalendar.get(Calendar.YEAR) - earlierCalendar.get(Calendar.YEAR);
    }

    /**
     * 计算指定两个时间之间的相差月份数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 相差月份数
     */
    public static int monthsBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = Calendar.getInstance();
        Calendar laterCalendar = Calendar.getInstance();
        earlierCalendar.setTime(earlierDate);
        laterCalendar.setTime(laterDate);
        int months = (laterCalendar.get(Calendar.YEAR) - earlierCalendar.get(Calendar.YEAR)) * 12;
        return months - earlierCalendar.get(Calendar.MONTH) + laterCalendar.get(Calendar.MONTH);
    }

    /**
     * 计算指定两个时间之间的相差天数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 相差天数
     */
    public static int daysBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = setTimeToCalendar(earlierDate, 0, 0, 0, 0);
        Calendar laterCalendar = setTimeToCalendar(laterDate, 0, 0, 0, 0);
        long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
        return (int) (dms / Times.MS_ONE_DAY);
    }

    /**
     * 计算指定两个时间之间的相差小时之差。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 小时之差
     */
    public static int hoursBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, 0, 0, 0);
        Calendar laterCalendar = setTimeToCalendar(laterDate, -1, 0, 0, 0);
        long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
        return (int) (dms / Times.MS_ONE_HOUR);
    }

    /**
     * 计算指定两个时间之间的相差分钟数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 分钟差
     */
    public static int minutesBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, -1, 0, 0);
        Calendar laterCalendar = setTimeToCalendar(laterDate, -1, -1, 0, 0);
        long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
        return (int) (dms / Times.MS_ONE_MINUTE);
    }

    /**
     * 计算指定两个时间之间的相差秒数。如果earlierDate晚于laterDate，则返回负值
     *
     * @param earlierDate 较早时间
     * @param laterDate   较晚时间
     * @return 秒差
     */
    public static long secondsBetween(Date earlierDate, Date laterDate) {
        Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, -1, -1, 0);
        Calendar laterCalendar = setTimeToCalendar(laterDate, -1, -1, -1, 0);
        long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
        return dms / Times.MS_ONE_SECOND;
    }

    /**
     * 创建指定值的日期
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        时
     * @param minute      分
     * @param second      秒
     * @param millisecond 毫秒
     * @return 日期
     */
    public static Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // 月份从0开始
        c.set(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, millisecond);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定年数后的日期值。若年数为负，则实际进行减操作
     *
     * @param date  原日期
     * @param years 年数
     * @return 计算后的新日期
     */
    public static Date addYears(Date date, int years) {
        if (years == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定月数后的日期值。若月数为负，则实际进行减操作。
     *
     * @param date   原日期
     * @param months 月数
     * @return 计算后的新日期
     */
    public static Date addMonths(Date date, int months) {
        if (months == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定天数后的日期值。若天数为负，则实际进行减操作。
     *
     * @param date 原日期
     * @param days 天数
     * @return 计算后的新日期
     */
    public static Date addDays(Date date, int days) {
        if (days == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定小时数后的日期值。若小时数为负，则实际进行减操作。
     *
     * @param date  原日期
     * @param hours 小时数
     * @return 计算后的新日期
     */
    public static Date addHours(Date date, int hours) {
        if (hours == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定分钟数后的日期值。若分钟数为负，则实际进行减操作。
     *
     * @param date    原日期
     * @param minutes 分钟数
     * @return 计算后的新日期
     */
    public static Date addMinutes(Date date, int minutes) {
        if (minutes == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    /**
     * 获取指定日期加上指定秒数后的日期值。若秒数为负，则实际进行减操作。
     *
     * @param date    原日期
     * @param seconds 秒数
     * @return 计算后的新日期
     */
    public static Date addSeconds(Date date, int seconds) {
        if (seconds == 0) {
            return date;
        }
        Calendar c = getCalendar(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 为指定日期设置年月日，返回新日期
     *
     * @param date  原日期
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 新日期
     */
    public static Date setDate(Date date, int year, int month, int day) {
        Calendar c = getCalendar(date);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // 月份从0开始
        c.set(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 为指定日期设置时分秒毫秒，返回新日期
     *
     * @param date        原日期
     * @param hour        时
     * @param minute      分
     * @param second      秒
     * @param millisecond 毫秒
     * @return 新日期
     */
    public static Date setTime(Date date, int hour, int minute, int second, int millisecond) {
        Calendar c = setTimeToCalendar(date, hour, minute, second, millisecond);
        return c == null ? null : c.getTime();
    }

    private static Calendar setTimeToCalendar(Date date, int hour, int minute, int second, int millisecond) {
        Calendar c = getCalendar(date);
        if (c == null) {
            return null;
        }
        if (hour >= 0) {
            c.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute >= 0) {
            c.set(Calendar.MINUTE, minute);
        }
        if (second >= 0) {
            c.set(Calendar.SECOND, second);
        }
        if (millisecond >= 0) {
            c.set(Calendar.MILLISECOND, millisecond);
        }
        return c;
    }

    /**
     * 获取指定日期集合中最早的日期
     *
     * @param dates 日期集合
     * @return 最早的日期
     */
    public static Date earliest(Date... dates) {
        Date result = null;
        for (Date date : dates) {
            if (result == null) {
                result = date;
            } else if (date.before(result)) {
                result = date;
            }
        }
        return result;
    }

    /**
     * 获取指定日期集合中最晚的日期
     *
     * @param dates 日期集合
     * @return 最晚的日期
     */
    public static Date latest(Date... dates) {
        Date result = null;
        for (Date date : dates) {
            if (result == null) {
                result = date;
            } else if (date.after(result)) {
                result = date;
            }
        }
        return result;
    }

    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH) + 1; // 月份从0开始
    }

    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断指定日期是否周末
     *
     * @param date 日期
     * @return 是否周末
     */
    public static boolean isWeekend(Date date) {
        int weekday = getCalendar(date).get(Calendar.DAY_OF_WEEK);
        return weekday == Calendar.SUNDAY || weekday == Calendar.SATURDAY;
    }

}
