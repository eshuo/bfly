package com.example.scalaD;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @Description
 * @Author wangshuo
 * @Date 2022-06-15 16:55
 * @Version V1.0
 */
public class NetworkFlow {


    public static void main(String[] args) {

        //ip 路径 设备  时间


        //信用分数表 t_credit_score

        //

        //记录表

        //重置分数表


        //日分数


        //


        // 三元表达式，? :
        ExpressionParser parser = new SpelExpressionParser();
        String ans = parser.parseExpression("1==3 ? '正确': '错误'").getValue(String.class);
        System.out.println("ifTheElse: " + ans);

        System.out.println("concat=" + parser.parseExpression("!'Hello World'.contains('World') and false").getValue(String.class));


    }


}
