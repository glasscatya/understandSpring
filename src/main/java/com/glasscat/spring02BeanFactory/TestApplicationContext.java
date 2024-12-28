package com.glasscat.spring02BeanFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class TestApplicationContext {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext servletWebServerApplicationContext
                            = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        for (String name : servletWebServerApplicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    @Configuration
    static class WebConfig {
        @Bean
        //所有ServletWeb的本质都是实现了这个接口。 我们这里使用Tomcat的实现 也就是使用Tomcat作为服务器
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }
        @Bean
        //将DispatcherServlet放到web服务中，他是MVC的入口
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();
        }
        @Bean
        //因为服务器是Tomcat 我们需要把DispatcherServlet注册到tomcat 告诉他将请求转发给谁
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean("/hello")
        //控制器
        public Controller controller() {
            return ((request, response) -> {
                response.getWriter().println("hello");
                return null;
            });
            //return new Controller() {
            //    @Override
            //    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            //        return null;
            //    }
            //};
        }
    }
}
