package com.glasscat.spring02BeanFactory.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LifeCycleBean {
    private static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);

    public LifeCycleBean() {
        log.debug("构造器被调用");
    }

    @Autowired
    public void autowire(@Value("${JAVA_HOME}")String home) {
        log.debug("依赖注入: {}", home);
    }
    @PostConstruct
    public void init() {
        log.debug("初始化");
    }
    @PreDestroy
    public void destroy() {
        log.debug("销毁");
    }
}
