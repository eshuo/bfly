package com.example.uruleserve;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;


@Configuration
@ImportResource({"classpath:urule-console-context.xml"})
@PropertySource(value = {"classpath:urule-console-context.properties"})
public class Config {
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourceLoader() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setOrder(1);
        return configurer;
    }


    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("datasource")
    public DataSource druid() {
        return new DruidDataSource();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource datasource() {
//        return DataSourceBuilder.create().build();
//    }
}