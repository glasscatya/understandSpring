package com.glasscat.spring03BeanPostProcessor;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

public class TestPostProcessor {
    public static void main(String[] args) {
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
        genericApplicationContext.registerBean("bean1", Bean1.class);
        genericApplicationContext.registerBean("bean2", Bean2.class);
        genericApplicationContext.registerBean("bean3", Bean3.class);
        //向BeanFactory添加解析器ContextAnnotationAutowireCandidateResolver 可以解析@Value
        genericApplicationContext.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //将AutowiredAnnotationBeanPostProcessor注册到BeanFactory中，现在容器拥有解析@Autowired的能力
        genericApplicationContext.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        //将CommonAnnotationBeanPostProcessor注册到BeanFactory中, 现在容器拥有解析@Resure @PreDestroy @PreConstruct的能力
        genericApplicationContext.registerBean(CommonAnnotationBeanPostProcessor.class);


        genericApplicationContext.refresh(); // 执行工厂后处理器，执行Bean后处理器，实例化所有bean

        Bean1 bean1 = genericApplicationContext.getBean(Bean1.class);
        System.out.println(bean1.bean2); // AutowiredAnnotationBeanPostProcessor添加后将解析
        System.out.println(bean1.bean3);
        genericApplicationContext.close();
    }
}
