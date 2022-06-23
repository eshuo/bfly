package com.example.Drools;

import com.example.springdemo.drools.IDroolsService;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author wangshuo
 * @Date 2022-06-20 10:11
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class droolsDemo {


    @Autowired
    private IDroolsService IDroolsService;


    @Test
    public void test1() {


        final int i = IDroolsService.calculateFare();

        System.err.println("===================>" + i);


    }


}
