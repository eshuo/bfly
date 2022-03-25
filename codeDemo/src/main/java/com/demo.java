package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author Ws
 * @Date 2022-03-24 11:35
 * @Version V1.0
 */
public class demo {


    private final static Pattern pageRegular = Pattern.compile("^(\\$\\{page\\..*\\})+");

    public static void main(String[] args) {
        final Matcher matcher = pageRegular.matcher("${page.xxx},1${page.123}");
//        System.out.println(matcher.matches());
//        System.out.println(matcher.group());
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));
        System.out.println(pageRegular.matcher("${page.123}12").matches());
        System.out.println(pageRegular.matcher("${page.aaa}").matches());
        System.out.println(pageRegular.matcher("${page.^^^}").matches());

    }

}
