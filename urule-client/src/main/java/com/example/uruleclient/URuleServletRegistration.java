package com.example.uruleclient;

import com.bstek.urule.KnowledgePackageReceiverServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * 此Servlet用于接收Urule服务端发布的知识包(不想用这个功能可以不配)
 */
@Component
public class URuleServletRegistration {
    @Bean
    public ServletRegistrationBean registerURuleServlet() {
        return new ServletRegistrationBean(new KnowledgePackageReceiverServlet(), "/knowledgepackagereceiver");
    }
}