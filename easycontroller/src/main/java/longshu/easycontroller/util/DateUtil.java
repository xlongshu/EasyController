package longshu.easycontroller.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateUtils
 *
 * @author longshu 2017年1月19日
 */
public final class DateUtil {

    public static String dateFormat = "yyyy-MM-dd";
    public static String timeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = timeFormat;

    /**
     * SimpleDateFormat创建成本高,所以静态成员化,配合ThreadLocal解决线程安全问题
     */
    private static ThreadLocal<DateFormat> defaultDateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATE_PATTERN, Locale.SIMPLIFIED_CHINESE);
        }
    };

    public static Date parseDate(String dateStr) {
        try {
            return defaultDateFormat.get().parse(dateStr);
        } catch (ParseException ignored) {
        }
        return null;
    }

    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException ignored) {
        }
        return null;
    }

    public static String toStr(Date date) {
        return defaultDateFormat.get().format(date);
    }

    public static String toStr(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
        return dateFormat.format(date);
    }

    public static synchronized void setDateFormat(String dateFormat) {
        if (StringUtils.isBlank(dateFormat))
            throw new IllegalArgumentException("timeFormat can not be blank.");
        DateUtil.dateFormat = dateFormat;
    }

    public static synchronized void setTimeFormat(String timeFormat) {
        if (StringUtils.isBlank(dateFormat))
            throw new IllegalArgumentException("dateFormat can not be blank.");
        DateUtil.timeFormat = timeFormat;
    }

}
