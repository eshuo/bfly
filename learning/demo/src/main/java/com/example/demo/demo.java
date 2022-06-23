package com.example.demo;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class demo {


    private final static Pattern IMAGE_PATTERN = Pattern.compile("(?i)\\S.*\\.(png|jpe?g|gif|svg)(\\?.*)?$");

    private final static Pattern VIDEO_PATTERN = Pattern.compile("(?i)\\S.*\\.(RM|RMVB|3GP|AVI|MPEG|MPG|MKV|DAT|ASF|WMV|FLV|MOV|MP4|OGG|OGM)(\\?.*)?$");
//    public static void main(String[] args) {
//
//        final int month = 5;
//
//        final int year = 2020;
//
//        String quarterlyStartTime = year + "-01-01";
//        String quarterlyEndTime = year + "-03-31";
//
//
//        switch (month) {
//            case 4:
//            case 5:
//            case 6:
//                quarterlyStartTime = year + "-04-01";
//                quarterlyEndTime = year + "-06-30";
//                break;
//            case 7:
//            case 8:
//            case 9:
//                quarterlyStartTime = year + "-07-01";
//                quarterlyEndTime = year + "-09-30";
//                break;
//            case 10:
//            case 11:
//            case 12:
//                quarterlyStartTime = year + "-10-01";
//                quarterlyEndTime = year + "-12-31";
//                break;
//            default:
//                break;
//        }
//
//        System.out.println(quarterlyEndTime+"----------"+quarterlyStartTime);
//
//    }

//    public static void main(String[] args) throws URISyntaxException, IOException {
////
////        String json ="{\"status\":0,\"result\":{\"location\":{\"lng\":116.29750599999994,\"lat\":40.06666006400466},\"formatted_address\":\"北京市海淀区南大街18号\",\"business\":\"回龙观\",\"addressComponent\":{\"country\":\"中国\",\"country_code\":0,\"country_code_iso\":\"CHN\",\"country_code_iso2\":\"CN\",\"province\":\"北京市\",\"city\":\"北京市\",\"city_level\":2,\"district\":\"海淀区\",\"town\":\"\",\"town_code\":\"\",\"adcode\":\"110108\",\"street\":\"南大街\",\"street_number\":\"18号\",\"direction\":\"东南\",\"distance\":\"51\"},\"pois\":[],\"roads\":[],\"poiRegions\":[{\"direction_desc\":\"内\",\"name\":\"百旺弘祥(弘祥1989)文创园\",\"tag\":\"公司企业;园区\",\"uid\":\"b44218c3a16c05d9bfa822d1\",\"distance\":\"0\"}],\"sematic_description\":\"百旺弘祥(弘祥1989)文创园内,北京上地物流公司-北门北122米\",\"cityCode\":131}}";
////
////
////        final GeocoderMessage geocoderMessage = JsonUtil.jsonToPojo(json,GeocoderMessage.class);
////        geocoderMessage.getStatus();
//
//
////        for (int i = 5; i < 30; i++) {
////
////            System.out.println("E"+i+"*G"+i);
////
////
////        }
////
////
////        String localhostMatcher  = "http[s]?:\\/\\/localhost\\:(\\d)+(.*)";
////
////        String urlMatcher = "http(s)?://([\\w-]+\\.)+\\w+(:\\d{1,5})?(.*)";
////
////        String url  = "http://localhost:8100/manage/groups";
////
//////
//////        if(url.matches(localhostPattern)){
//////            url =   url.replaceAll(localhostPattern,"");
//////        }else if(url.matches(urlPattern)){
//////            url =   url.replaceAll(urlPattern,"");
//////        }
////
////
////
////        Pattern localhostPattern = Pattern.compile(localhostMatcher );
////        Pattern urlPattern = Pattern.compile(urlMatcher);
////        Matcher lMatch = localhostPattern.matcher(url);
////        Matcher urlMatch = urlPattern.matcher(url);
////        if(lMatch.find()){
////           url  = lMatch.group(2);
////        }else if (urlMatch.find()){
////            url = urlMatch.group(4);
////        }
////
////
////        System.out.println(url);
////
////
////
////
////
////
////
////
////
////        url = new URI(url).getPath();
////
////
////        System.out.println(url);
////
////
////        String demo1 = ".*/redirect/.*";
////
////        System.out.println("https://api.zhongguozaisheng.com/api/redirect/esign/face".matches(demo1));
////
////
////
////        System.out.println(null !=null);
//
//
////        String filePath1 = "D:\\mp4\\OSOSSFOS202108191703443210000004.mp4";
////        String filePath2 = "D:\\mp4\\OSOSSFOS202108191704443210000003.mp4";
////        String filePath3 = "D:\\mp4\\OSOSSFOS202108191707443210000002.mp4";
////        String filePath4= "D:\\mp4\\OSOSSFOS2021081917034432100000041.mp4";
////
////
////        System.out.println(DigestUtils.md5Hex(new FileInputStream(filePath1)));
////        System.out.println(DigestUtils.md5Hex(new FileInputStream(filePath2)));
////        System.out.println(DigestUtils.md5Hex(new FileInputStream(filePath3)));
////        System.out.println(DigestUtils.md5Hex(new FileInputStream(filePath4)));
//
//
////        System.out.println(IMAGE_PATTERN.matcher("aaaa.png").matches());
////        System.out.println(IMAGE_PATTERN.matcher("aaaa.PNG").matches());
////        System.out.println(VIDEO_PATTERN.matcher("aaaa.RMVB").matches());
////        System.out.println(VIDEO_PATTERN.matcher("aaaa.rmvb").matches());
////        System.out.println(VIDEO_PATTERN.matcher("aaaa.mp4").matches());
////        System.out.println(VIDEO_PATTERN.matcher("aaaa.MP4").matches());
//
//
//
////        final BigDecimal settleTotal = new BigDecimal("97672.455").setScale(-8, RoundingMode.HALF_UP);
////
////        Double d = 97672.455;
////        final BigDecimal bigDecimal = BigDecimal.valueOf(d);
////        System.out.println(settleTotal.compareTo(bigDecimal) != 0);
//
////        String[] strings = new String[]{"1.mp4","1.jpg","2.mp4","2.jpg"};
////        if (null != strings) {
////            strings = Arrays.stream(strings).sorted((o1, o2) -> {
////                if (VIDEO_PATTERN.matcher(o1).matches()) {
////                    return -1;
////                } else if (VIDEO_PATTERN.matcher(o2).matches()) {
////                    return 1;
////                }
////                return 0;
////            }).toArray(String[]::new);
////        }
////
////
////        for (String string : strings) {
////            System.out.println(string);
////        }
//
//
//
////        Pattern mobileR = Pattern.compile("([1\\d]\\d{9})");
////
////        String mobile ="13363206099 18931534678 18932526578 13393050903";
////
////        final Matcher matcher = mobileR.matcher(mobile);
////
////       if( matcher.find()){
////           final String group = matcher.group(1);
////       }
////        final boolean matches = matcher.matches();
//
//
//
//        for (int i = 5; i < 35; i++) {
//            System.out.print("G"+i+"*E"+i+"+");
//        }
//
//
//
//
//    }



    public static void main(String[] args) {
//        System.out.println((getnowEndTime().getTime() - System.currentTimeMillis()) / 1000L);



        System.out.println(toShortId("OSOSSFOS202201110101011317000024"));
        System.out.println(toLongId("OSOSSFOS1UHSNGQTARL00O"));
        System.out.println(DigestUtils.md5("OSOSSFOS1UHSNGQTARL00O"));

    }

    public static Date getnowEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }


    public static String toShortId(String id) {
        if (id == null) {
            return null;
        }
        final String hex = Long.toUnsignedString(Long.parseLong(id.substring(10, 26)), 32);
        final String hex2 = Long.toUnsignedString(Long.parseLong(id.substring(26)), 32);
        return id.substring(0, 8).concat(hex.toUpperCase()).concat(StringUtils.leftPad(hex2.toUpperCase(), 3, '0'));
    }

    public static String toLongId(String id) {
        if (id == null) {
            return null;
        }
        if (id.length() == 32) {
            return id;
        }
        final String hex = Long.toUnsignedString(Long.parseLong(id.substring(8, 19).toLowerCase(), 32), 10);
        final String hex2 = Long.toUnsignedString(Long.parseLong(id.substring(19).toLowerCase(), 32), 10);
        return id.substring(0, 8).concat("20").concat(hex).concat(StringUtils.leftPad(hex2, 6, '0'));
    }
}
