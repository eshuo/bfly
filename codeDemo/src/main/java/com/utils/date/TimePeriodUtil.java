package com.utils.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimePeriodUtil {

    public static final String TODAY = "today";

    public static final String YESTERDAY = "yesterday";

    public static final String THIS_WEEK = "this_week";

    public static final String THIS_MONTH = "this_month";

    /**
     * 上月
     */
    public static final String LAST_MONTH = "last_month";
    /**
     * 本季度
     */
    public static final String THIS_QUARTER = "this_quarter";

    /**
     * 本年
     */
    public static final String THIS_YEAR = "this_year";

    /**
     * 去年
     */
    public static final String LAST_YEAR = "last_year";

    private static final Map<String, Period> TIMESTAMP = new ConcurrentHashMap<>();

    private static final Map<String, Period> NUMBER_FORMAT = new ConcurrentHashMap<>();

    private static final Map<String, Period> DAT_FORMAT = new ConcurrentHashMap<>();

    static {
        dayRun();
        numberRun();
        timestampRun();
    }

    /**
     * 时间格式：
     * 起始：20190612
     * 终止：20190612
     *
     * @return
     */
    public static Map<String, Period> getDayFormatPeriod() {
        dayRun();
        return DAT_FORMAT;
    }

    /**
     * 时间格式：
     * 起始：20190612000000
     * 终止：20190612999999
     *
     * @return
     */
    public static Map<String, Period> getNumberFormatPeriod() {
        numberRun();
        return NUMBER_FORMAT;
    }

    /**
     * 时间格式：1555996654000
     *
     * @return
     */
    public static Map<String, Period> getTimestampFormatPeriod() {
        timestampRun();
        return TIMESTAMP;
    }

    private static void dayRun() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            long day = 24 * 60 * 60 * 1000;

            //今天
            String today = dateFormat.format(new Date());
            String todayStart = today;
            String todayEnd = today;
            DAT_FORMAT.put(TODAY, new Period(todayStart, todayEnd));

            //昨天
            long todayTimes = dateFormat.parse(today).getTime();
            String yesterday = dateFormat.format(new Date(todayTimes - day));
            String yesterdayStart = yesterday;
            String yesterdayEnd = yesterday;
            DAT_FORMAT.put(YESTERDAY, new Period(yesterdayStart, yesterdayEnd));

            //本周
            cal.set(Calendar.DAY_OF_WEEK, 1);
            String thisWeek = dateFormat.format(getFirstDayOfWeek(new Date()));
            String thisWeekStart = thisWeek;
            String thisWeekEnd = today;
            DAT_FORMAT.put(THIS_WEEK, new Period(thisWeekStart, thisWeekEnd));

            //本月
            String monthStartStr = getFirstDayOfMonth1(0, cal.get(Calendar.MONTH) + 1);
            String thisMonthStart = monthStartStr;
            String thisMonthEnd = today;
            DAT_FORMAT.put(THIS_MONTH, new Period(thisMonthStart, thisMonthEnd));

            //上月
            final Date thisMonthStartDate = DateUtil.stringToDate(thisMonthStart, DateStyle.YYYYMMDD);
            final String lastMonthStart = DateUtil.dateToString(DateUtil.addMonth(thisMonthStartDate, -1), DateStyle.YYYYMMDD);
            final String lastMonthEnd = DateUtil.dateToString(DateUtil.addDay(thisMonthStartDate, -1), DateStyle.YYYYMMDD);
            DAT_FORMAT.put(LAST_MONTH, new Period(lastMonthStart, lastMonthEnd));

            //本季
            String quarterStartStr = getQuarterStart();
            String thisQuarterStart = quarterStartStr;
            String thisQuarterEnd = today;
            DAT_FORMAT.put(THIS_QUARTER, new Period(thisQuarterStart, thisQuarterEnd));

            //本年
            int year = cal.get(Calendar.YEAR);
            String yearStartStr = getFirstDayOfMonth1(year, 1);
            String thisYearStart = yearStartStr;
            String thisYearEnd = today;
            DAT_FORMAT.put(THIS_YEAR, new Period(thisYearStart, thisYearEnd));

            //去年
            final Date date = DateUtil.stringToDate(thisYearStart, DateStyle.YYYYMMDD);
            final String lastYearStart = DateUtil.dateToString(DateUtil.addYear(date, -1), DateStyle.YYYYMMDD);
            final String lastYearEnd = DateUtil.dateToString(DateUtil.addDay(date, -1), DateStyle.YYYYMMDD);
            DAT_FORMAT.put(LAST_YEAR, new Period(lastYearStart, lastYearEnd));

        } catch (Exception e) {
            throw new RuntimeException("时间段获取异常!");
        }
    }

    private static void numberRun() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            long day = 24 * 60 * 60 * 1000;

            //今天
            String today = dateFormat.format(new Date());
            String todayStart = today + "000000";
            String todayEnd = today + "999999";
            NUMBER_FORMAT.put(TODAY, new Period(todayStart, todayEnd));

            //昨天
            long todayTimes = dateFormat.parse(today).getTime();
            String yesterday = dateFormat.format(new Date(todayTimes - day));
            String yesterdayStart = yesterday + "000000";
            String yesterdayEnd = yesterday + "999999";
            NUMBER_FORMAT.put(YESTERDAY, new Period(yesterdayStart, yesterdayEnd));

            //本周
            cal.set(Calendar.DAY_OF_WEEK, 1);
            String thisWeek = dateFormat.format(getFirstDayOfWeek(new Date()));
            String thisWeekStart = thisWeek + "000000";
            String thisWeekEnd = today + "999999";
            NUMBER_FORMAT.put(THIS_WEEK, new Period(thisWeekStart, thisWeekEnd));

            //本月
            String monthStartStr = DateUtil.dateToString(DateUtil.addMonth(new Date(), 0), DateStyle.YYYY_MM).replace("-", "") + "01";
            String thisMonthStart = monthStartStr + "000000";
            String thisMonthEnd = today + "999999";
            NUMBER_FORMAT.put(THIS_MONTH, new Period(thisMonthStart, thisMonthEnd));
            //上月
            final Date thisMonthStartDate = DateUtil.stringToDate(monthStartStr, DateStyle.YYYYMMDD);
            final String lastMonthStart = DateUtil.dateToString(DateUtil.addMonth(thisMonthStartDate, -1), DateStyle.YYYYMMDD);
            final String lastMonthEnd = DateUtil.dateToString(DateUtil.addDay(thisMonthStartDate, -1), DateStyle.YYYYMMDD);
            NUMBER_FORMAT.put(LAST_MONTH, new Period(lastMonthStart+ "000000", lastMonthEnd+ "999999"));
            //本季
            String quarterStartStr = getQuarterStart();
            String thisQuarterStart = quarterStartStr + "000000";
            String thisQuarterEnd = today + "999999";
            NUMBER_FORMAT.put(THIS_QUARTER, new Period(thisQuarterStart, thisQuarterEnd));

            //本年
            int year = cal.get(Calendar.YEAR);
            String yearStartStr = DateUtil.dateToString(DateUtil.addMonth(new Date(), 0), DateStyle.YYYY_MM).substring(0, 4) + "0101";
            String thisYearStart = yearStartStr + "000000";
            String thisYearEnd = today + "999999";
            NUMBER_FORMAT.put(THIS_YEAR, new Period(thisYearStart, thisYearEnd));

            //去年
            final Date date = DateUtil.stringToDate(yearStartStr, DateStyle.YYYYMMDD);
            final String lastYearStart = DateUtil.dateToString(DateUtil.addYear(date, -1), DateStyle.YYYYMMDD);
            final String lastYearEnd = DateUtil.dateToString(DateUtil.addDay(date, -1), DateStyle.YYYYMMDD);
            NUMBER_FORMAT.put(LAST_YEAR, new Period(lastYearStart + "000000", lastYearEnd + "999999"));

        } catch (Exception e) {
            throw new RuntimeException("时间段获取异常!");
        }
    }

    private static void timestampRun() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            long day = 24 * 60 * 60 * 1000;

            //今天
            String today = dateFormat.format(new Date());
            long todayStart = dateFormat.parse(today).getTime();
            long todayEnd = todayStart + day;
            TIMESTAMP.put(TODAY, new Period(String.valueOf(todayStart), String.valueOf(todayEnd)));

            //昨天
            long yesterdayStart = todayStart - day;
            long yesterdayEnd = todayStart - 1000;
            TIMESTAMP.put(YESTERDAY, new Period(String.valueOf(yesterdayStart), String.valueOf(yesterdayEnd)));

            //本周
            String lastWeek = dateFormat.format(getFirstDayOfWeek(new Date()));
            long thisWeekStart = dateFormat.parse(lastWeek).getTime();
            long thisWeekEnd = todayEnd;
            TIMESTAMP.put(THIS_WEEK, new Period(String.valueOf(thisWeekStart), String.valueOf(thisWeekEnd)));

            //本月
            String monthStartStr = getFirstDayOfMonth1(0, cal.get(Calendar.MONTH) + 1);
            long thisMonthStart = dateFormat.parse(monthStartStr).getTime();
            long thisMonthEnd = todayEnd;
            TIMESTAMP.put(THIS_MONTH, new Period(String.valueOf(thisMonthStart), String.valueOf(thisMonthEnd)));
            //上月
            final long lastMonthStart = DateUtil.addMonth(new Date(thisMonthStart), -1).getTime();
            final long lastMonthEnd = thisMonthStart - 1000;
            TIMESTAMP.put(LAST_MONTH, new Period(String.valueOf(lastMonthStart), String.valueOf(lastMonthEnd)));
            //本季
            String quarterStartStr = getQuarterStart();
            long quarterStart = dateFormat.parse(quarterStartStr).getTime();
            long quarterEnd = todayEnd;
            TIMESTAMP.put(THIS_QUARTER, new Period(String.valueOf(quarterStart), String.valueOf(quarterEnd)));

            //本年
            int year = cal.get(Calendar.YEAR);
            String yearStartStr = getFirstDayOfMonth1(year, 1);
            long yearStart = dateFormat.parse(yearStartStr).getTime();
            long yearEnd = todayEnd;
            TIMESTAMP.put(THIS_YEAR, new Period(String.valueOf(yearStart), String.valueOf(yearEnd)));

            //去年
            final Date lastYearDate = DateUtil.addYear(new Date(yearStart), -1);
            long lastYearEnd = yearStart - 1000;
            //final Date lastYearEnd = DateUtil.addDay(new Date(yearStart), -1);
            TIMESTAMP.put(LAST_YEAR, new Period(String.valueOf(lastYearDate.getTime()), String.valueOf(lastYearEnd)));
        } catch (Exception e) {
            throw new RuntimeException("时间段获取异常!");
        }
    }

    private static Date getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    private static String getFirstDayOfMonth1(int year, int month) {

        Calendar cal = Calendar.getInstance();
        if (year != 0) {
            //设置年份
            cal.set(Calendar.YEAR, year);
        }

        if (month != 0) {
            //设置月份
            cal.set(Calendar.MONTH, month - 1);
        }
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    private static String getQuarterStart() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.MONTH) + 1;

        if (i >= 1 && i <= 3) {
            return getFirstDayOfMonth1(0, 1);
        } else if (i >= 4 && i <= 6) {
            return getFirstDayOfMonth1(0, 4);
        } else if (i >= 7 && i <= 9) {
            return getFirstDayOfMonth1(0, 7);
        } else {
            return getFirstDayOfMonth1(0, 10);
        }
    }
}
