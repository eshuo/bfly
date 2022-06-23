package com.example.springdemo.drools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wangshuo
 * @Date 2022-06-20 13:57
 * @Version V1.0
 */
@RestController
@RequestMapping("/rule")
public class IDrollsColl {


    @Autowired
    private IDroolsService iDroolsService;


    @RequestMapping("/all")
    public int getAll() {

        return iDroolsService.calculateFare();
    }


}
