package com.example.springdemo.drools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description
 * @Author wangshuo
 * @Date 2022-06-20 13:42
 * @Version V1.0
 */
public class runnerDemo {


    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(IDroolsService.class);
        final IDroolsService contextBean = context.getBean(IDroolsService.class);
        final int i = contextBean.calculateFare();

        System.out.println("==========>" + i);

    }
}
