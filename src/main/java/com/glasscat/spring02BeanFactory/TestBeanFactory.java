package com.glasscat.spring02BeanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class TestBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //手动创建一个BeanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton")
                                                                     .getBeanDefinition();
        //将BeanDefinition注册进BeanFactory 但是你会发现，config类上的注解并没有被解析。
        // 原因在于这些功能不是在BF上，而是在BF后处理器上。
        beanFactory.registerBeanDefinition("config", beanDefinition);
        //通过工具类给BF中添加BF后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //让BF执行这些后处理器
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class)
                   .values()
                   .forEach(beanFactoryPostProcessor -> {
                       beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
                   });

        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        beanFactory.getBeansOfType(BeanPostProcessor.class)
                   .values()
                   .forEach(beanFactory::addBeanPostProcessor);

        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(TestBeanFactory.class);

        @Autowired
        private Bean2 bean2;

        static {
            log.debug("bean1被加载");
        }

        public Bean1() {
            System.out.println("bean1初始化...");
        }

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(TestBeanFactory.class);

        static {
            log.debug("bean2被加载");
        }
    }
}
