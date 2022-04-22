package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Ws
 * @Date 2022-03-24 11:35
 * @Version V1.0
 */
public class demo {


    private final static Pattern pageRegular = Pattern.compile("^(\\$\\{page\\..*\\})+");

    public static void main(String[] args) {
//        final Matcher matcher = pageRegular.matcher("${page.xxx},1${page.123}");
////        System.out.println(matcher.matches());
////        System.out.println(matcher.group());
//        System.out.println(matcher.find());
//        System.out.println(matcher.group(1));
//        System.out.println(pageRegular.matcher("${page.123}12").matches());
//        System.out.println(pageRegular.matcher("${page.aaa}").matches());
//        System.out.println(pageRegular.matcher("${page.^^^}").matches());


        List<PunchPrize> punchPrizeList = new ArrayList<>();


        for (int i = 0; i < 10; i++) {

            PunchPrize p = new PunchPrize();
            p.setDays(i);
            punchPrizeList.add(p);
        }

        for (int i = 0; i < 2; i++) {
            PunchPrize p = new PunchPrize();
            punchPrizeList.add(p);
        }

        PunchPrize p = new PunchPrize();
        p.setDays(15);

        punchPrizeList.add(p);

        final Map<Integer, List<PunchPrize>> collect = punchPrizeList.stream().filter(s -> null != s.getDays()).collect(Collectors.groupingBy(PunchPrize::getDays));
        final long count = punchPrizeList.stream().filter(s -> null != s.getDays()).collect(Collectors.groupingBy(PunchPrize::getDays)).values().stream().filter(s -> s.size() > 1).count();

        System.err.println(collect.size());


    }


    public static class PunchPrize implements Serializable {
        private static final long serialVersionUID = 2777803689727139739L;


        /**
         * 天数
         */
        private Integer days;

        /**
         * 图标
         */
        private String icon;

        /**
         * 名称
         */
        private String name;

        /**
         *
         */
        private String type;

        /**
         * 目标Id
         */
        private String targetId;


        /**
         * 奖品名称
         */
        private String prizeName;

        /**
         * 奖品到期时间
         */
        private Long expireTime;

        /**
         * 奖品金额
         */
        private Double money;


        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }
    }


}
