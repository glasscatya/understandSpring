package com.glasscat.spring01ApplocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class SpringMain {
    private static final Logger log = LoggerFactory.getLogger(SpringMain.class);

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringMain.class, args);
        //读取资源
        Resource[] resources = applicationContext.getResources("classpath:application.properties");
        System.out.println(Arrays.stream(resources).toList());
        //获取环境变量
        String javaHome = applicationContext.getEnvironment().getProperty("java.version");
        System.out.println(javaHome);
        //事件发布
        //applicationContext.publishEvent(new UserRegisteredEvent(applicationContext));
        applicationContext.getBean(Component01.class).register();
    }
}
