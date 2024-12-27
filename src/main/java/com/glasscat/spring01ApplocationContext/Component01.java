package com.glasscat.spring01ApplocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component01 {

    private static final Logger log = LoggerFactory.getLogger(Component01.class);
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void register() {
        log.debug("发布事件");
        eventPublisher.publishEvent(new UserRegisteredEvent(this));
    }
}
