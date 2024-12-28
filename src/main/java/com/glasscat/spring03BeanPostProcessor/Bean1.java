package com.glasscat.spring03BeanPostProcessor;


import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Bean1 {
    @Autowired
    public Bean2 bean2;
    @Resource
    public Bean3 bean3;

    private String home;

    @Autowired
    public void setHome(@Value("${JAVA_HOME}") String home) {
        this.home = home;
        System.out.println("home:" + home);
    }

    public Bean1() {
        System.out.println("构造器被调用");
    }
    @PreDestroy
    public void destroy() {
        System.out.println("Bean1销毁");
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "bean2=" + bean2 +
                ", bean3=" + bean3 +
                ", home='" + home + '\'' +
                '}';
    }
}
