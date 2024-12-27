package com.glasscat.spring02BeanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class TestXMLBeanDefinitionReader {

    public static void main(String[] args) {
        //演示下手工读取xml配置
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //将beanFactory注册到reader中
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        //指定reader要读取的xml配置文件 读取到后将自动解析成beanDefinition 注册到beanFactory中
        reader.loadBeanDefinitions(new ClassPathResource("b1.xml"));

        for(String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }


    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(TestXMLBeanDefinitionReader.class);

        private Bean2 bean2;

        static {
            log.debug("bean1被加载");
        }

        public Bean1() {
            System.out.println("bean1初始化...");
        }

        public void setBean2(Bean2 bean2) {
            this.bean2 = bean2;
        }

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(TestXMLBeanDefinitionReader.class);

        static {
            log.debug("bean2被加载");
        }
    }
}
