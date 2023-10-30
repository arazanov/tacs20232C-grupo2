package com.springboot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootRestApplication extends SpringBootServletInitializer {

    private static ApplicationContext applicationContext;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootRestApplication.class);
    }

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(SpringBootRestApplication.class, args);
        //checkBeansPresence("userRepository", "passwordEncoder");
    }

    // test para ver que las annotations (@) estén bien
    private static void checkBeansPresence(String... beans) {
        for (String beanName : beans) {
            System.out.println("Is " + beanName + " in ApplicationContext: " +
                    applicationContext.containsBean(beanName));
        }
    }
}