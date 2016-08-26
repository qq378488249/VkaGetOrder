package cc.chenghong.vkagetorder.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cc.chenghong.vkagetorder.bean.Page;

public class DateUtils {

    public static final String datePattern = "yyyy-MM-dd";
    public static final String datePattern1 = "yyyyMMddHHmmss";

    public static final String dateCnPattern = "yyyy年MM月dd日HH时mm分";
Page

    public static String formatBirthday(String dateString) {
        if (dateString == null) return null;

        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
        SimpleDateFormat df2 = new SimpleDateFormat("MM-dd", Locale.UK);

        try {
            Date date = df.parse(dateString);
            return df2.format(date);
        } catch (ParseException e) {
            //throw new RuntimeException(e);
            return null;
        }
    }

    public static Date thisYearBirthday(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        try {
            Date date = sdf.parse(birthday);
            date.setYear(new Date().getYear());
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算日期相差几天
     *
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 字符串转换成日期字符串
     *
     * @param str
     * @return date
     */
    public static String StrToDateStr(String str, String datePattern) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (datePattern == null) datePattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
        return df.format(date);
    }


    public static String getBirthdayAfterDays(int days) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String dateFormat(Date date, String datePattern) {
        if (date == null) return "";
        if (datePattern == null) datePattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
        return df.format(date);
    }

    public static String dateFormatNow(String datePattern) {
        if (datePattern == null) datePattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(datePattern, Locale.UK);
        return df.format(new Date());
    }

    public static Date getYestoday() {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - 1);

        return now.getTime();
    }

    public static Date getAfterMonth(int month) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);

        return now.getTime();
    }

    public static Date getAfterDay(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + day);

        return now.getTime();
    }

    public static Date getBeforeDay(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - day);

        return now.getTime();
    }

    public static Date getAfterMinute(int minute) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minute);

        return now.getTime();
    }

    public static Date getBeforeMinute(int minute) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minute);

        return now.getTime();
    }

    public static Date getAfterHours(int hours) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hours);

        return now.getTime();
    }

    public static Date getAfterSecond(int Second) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + Second);

        return now.getTime();
    }

    public static int getHours(Date start, Date end) {
        long time = end.getTime() - start.getTime();
        long hours = time / (1000 * 60 * 60);
        return (int) hours;
    }

    public static int compareDate(Date date, Date now) {
        String nowString = dateFormat(now, datePattern);
        String dateString = dateFormat(date, datePattern);

        Date nowDate = null, myDate = null;
        SimpleDateFormat myFormatter = new SimpleDateFormat(datePattern);

        try {
            nowDate = myFormatter.parse(nowString);
            myDate = myFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        int day = (int) ((myDate.getTime() - nowDate.getTime()) / (24 * 60 * 60 * 1000));
        return day;
    }

    public static int compare(Date date1, Date date2) {
        String date1String = dateFormat(date1, datePattern);
        String date2String = dateFormat(date2, datePattern);

        SimpleDateFormat myFormatter = new SimpleDateFormat(datePattern);

        try {
            date1 = myFormatter.parse(date1String);
            date2 = myFormatter.parse(date2String);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        int day = (int) ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
        return day;
    }


    public static java.sql.Date toSqlDate(Calendar c) {
        return new java.sql.Date(c.getTimeInMillis());
    }

    public static Calendar getFirstDayOfWeek() {
        Calendar monday = Calendar.getInstance();
        return getADayOfWeek(monday, Calendar.MONDAY);
    }

    public static Calendar getFirstDayOfWeek(Calendar day) {
        Calendar monday = (Calendar) day.clone();
        return getADayOfWeek(monday, Calendar.MONDAY);
    }

    public static Calendar getLastDayOfWeek() {
        Calendar sunday = Calendar.getInstance();
        return getADayOfWeek(sunday, Calendar.SUNDAY);
    }

    public static Calendar getLastDayOfWeek(Calendar day) {
        Calendar sunday = (Calendar) day.clone();
        return getADayOfWeek(sunday, Calendar.SUNDAY);
    }

    private static Calendar getADayOfWeek(Calendar day, int dayOfWeek) {
        int week = day.get(Calendar.DAY_OF_WEEK);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        if (week == dayOfWeek)
            return day;
        int diffDay = dayOfWeek - week;
        if (week == Calendar.SUNDAY) {
            diffDay -= 7;
        } else if (dayOfWeek == Calendar.SUNDAY) {
            diffDay += 7;
        }
        day.add(Calendar.DATE, diffDay);

        return day;
    }

    /**
     * 得到本月的第一天
     *
     * @return
     */
    public static Date getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 得到本月的最后一天
     *
     * @return
     */
    public static Date getMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date nextDay(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 格式化时间为yyyy-MM-dd 00:00:00
     */
    public static Date dayStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 格式化时间为yyyy-MM-dd 00:00:00
     */
    public static Date dayEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获得指定日期的后一天
     *
     * @param date
     * @return
     */
    public static String getTomorrowString(String date) {
        String str = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            str = sdf.format(nextDay(sdf.parse(date)));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return str;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int reduce) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - reduce);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 订单生成时间
     *
     * @return
     */
    public static String getSystemTimeForStart() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern1);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 订单失效时间
     *
     * @return
     */
    public static String getSystemTimeForExpire() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern1);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static void main(String[] args) {
        //BigDecimal cashFee = new BigDecimal(100).divide(new BigDecimal(100));
        //System.out.println(dateFormatNow("yyyy-MM-dd HH:mm:ss"));
        //String str = getSpecifiedDayBefore("2015-12-15", 1);
        System.out.println(getSystemTimeForExpire());
        System.out.println(getTheCurrentHour(true)+getTheCurrentMinute()+"");
        Log.e("date", getTheCurrentHour(true) + getTheCurrentMinute() + "");
    }

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取指定格式的当前时间
     *
     * @param str 时间格式
     * @return 字符串类型的当前时间
     */
    public static String getDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(new Date());
    }

    /**
     * 获取"年-月-日 时(24小时制):分:秒"格式的当前时间
     *
     * @return 字符串类型的当前时间
     */
    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 返回时间1是否大于时间2
     *
     * @param t1
     * @param t2
     * @param str_sdf 时间格式
     * @return true s1大于或等于s2 ，否则反之
     * @throws ParseException
     */
    public static boolean date1CompareDate2(String t1, String t2, String str_sdf) {
        if (t1.equals(t2)) {
            return true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(str_sdf);
        long result = 0;
        try {
            Date d1 = sdf.parse(t1);
            Date d2 = sdf.parse(t2);
            result = d1.getTime() - d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }

    /**
     * 获取当前小时
     * @param is24 是否是24小时制
     * @return
     */
    public static int getTheCurrentHour(boolean is24) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = is24 ? new SimpleDateFormat("HH") : new SimpleDateFormat("hh");
        return Integer.valueOf(simpleDateFormat.format(new Date()));
    }

    /**
     * 获取当前分钟
     * @return
     */
    public static int getTheCurrentMinute() {
        return Integer.valueOf(new SimpleDateFormat("mm").format(new Date()));
    }

}
