package com.example.springdemo.drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wangshuo
 * @Date 2022-06-20 10:33
 * @Version V1.0
 */
@Service
public class IDroolsService {

    @Autowired
    private KieContainer kContainer;

    public int calculateFare() {
        KieSession kieSession = kContainer.newKieSession();
        final int i = kieSession.fireAllRules();
        kieSession.dispose();
        return i;
    }
}
