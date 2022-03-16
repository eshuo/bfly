package info.bfly.core.util;

import info.bfly.core.annotations.Log;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 项目的共用方法
 *
 * @author yinjunlu
 */
@Component
public class PublicUtils {
    @Log
    private static Logger log;
    /**
     * 把"<"、">"转义
     *
     * @param input
     * @return
     */
    public static final String escapeHTMLTags(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        StringBuffer buf = new StringBuffer(input.length());
        char ch = ' ';
        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            if (ch == '<') {
                buf.append("&lt;");
            } else if (ch == '>') {
                buf.append("&gt;");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    /**
     * 时间格式化 格式：yyyyMMddHHmmss yyyy-MM-dd yyyy-MM-dd HH:mm:ss等
     *
     * @param dateFormat
     * @return
     */
    public static String formatDate(String dateFormat) {
        String rdate;
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        rdate = date.format(new java.util.Date());
        return rdate;
    }

    /**
     * 当前时间，精确到秒
     *
     * @return
     */
    public static String getCurrentDatetimeByString() {
        String datetime = new Timestamp(System.currentTimeMillis()).toString();
        return datetime.substring(0, 19);
    }

    /*********************** 时间处理 **************************/
    /**
     * 根据固定的时间格式的字符串返回时间
     *
     * @param dateString
     * @return
     */
    public static Date getDatebyString(String dateString) {
        SimpleDateFormat dateStr = new SimpleDateFormat();
        try {
            Date date = dateStr.parse(dateString);
            return date;
        } catch (ParseException e) {
            log.debug(e.getMessage());
            return new Date();
        }
    }

    /*************** 随机数 ***********************/
    /**
     * 给定字符和长度生成随机数
     *
     * @param source
     * @param length
     * @return
     */
    public static String getRandom(String source, int length) {
        char[] sourceChar = source.toCharArray();
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[RandomUtils.nextInt(0,sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 数字随机数
     *
     * @param length
     * @return
     */
    public static String getrandomInt(int length) {
        return PublicUtils.getRandom("0123456789", length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由数字和大小写字母混合组成
     *
     * @param length
     * @return
     */
    public static String getrandomString(int length) {
        return PublicUtils.getRandom("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
    }

    /**
     * 根据给定的日期，返回给定的字符串， 返回 字符串的形式是：yyyy-MM-dd HH:mm:ss
     *
     * @param date 要格式化的日期
     * @return 将日期格式化后返回的字符串，以这中格式返回：yyyy-MM-dd HH:mm:ss
     * @throws Exception
     */
    public final static String getStrByDate(java.util.Date date, String format) throws Exception {
        try {
            if (date == null || PublicUtils.isEmpty(format)) {
                return null;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.format(date);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 判断字符串是否为空
     * ，空true，否则false
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        try {
            if (str == null) {
                return true;
            } else if ("".equals(str.trim())) {
                return true;
            } else return "null".equals(str.trim());
        } catch (Exception e) {
            log.debug(e.getMessage());
            return true;
        }
    }

    /**
     * 判断字Object是否为空
     * ，空true，否则false
     *
     * @return
     */
    public static boolean isObjectEmpty(Object obj) {
        try {
            if (obj == null) {
                return true;
            } else if ("".equals(obj.toString())) {
                return true;
            } else return "null".equals(obj.toString());
        } catch (Exception e) {
            log.debug(e.getMessage());
            return true;
        }
    }

    /*********************** 空判断 *****************************/
    public static void main(String[] args) {
        try {
            Class<?> test = Class.forName("info.bfly.core.util.PublicUtils");
            Method[] methods = test.getMethods();
            for (Method method : methods) {
                log.info(method.getName());
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
        }
    }

    /**
     * 为给定的日历字段添加或减去指定的时间
     *
     * @param amount 正数为添加|负数为减少
     * @return
     */
    public static Date setDatelessormore(int dateType, int amount) {
        Calendar calendar = Calendar.getInstance();
        if (amount == 0) {
            return new Date();
        }
        calendar.add(dateType, amount);
        return calendar.getTime();
    }

    /**
     * 为给定的日历字段添加或减去指定的小时
     *
     * @param amount
     * @return
     */
    public static Date setDatelessormoreHour(int amount) {
        return PublicUtils.setDatelessormore(Calendar.HOUR_OF_DAY, amount);
    }

    /**
     * 传入起始和结束数字返回对应的数组
     *
     * @param beginNum
     * @param endNum
     * @return
     */
    public static List<Long> getNumArray(long beginNum, long endNum) {
        if (endNum < beginNum) return null;
        List<Long> num = new ArrayList<Long>();
        for (long i = beginNum; i <= endNum; i++) {
            num.add(i);
        }
        return num;
    }

    /**
     * 传入字符串，返回对应的数字
     * @param num
     * @param defaultnum
     * @return
     */
    public static int getInt(String num,int defaultnum){
        return NumberUtils.toInt(num,defaultnum);
    }

    /**
     * 根据url返回TermId
     * @param url
     * @return
     */
    public static String getTermIdFromUrl(String url){
        return StringUtils.substringAfterLast(url,"/").replaceFirst("-list","");
    }

}
