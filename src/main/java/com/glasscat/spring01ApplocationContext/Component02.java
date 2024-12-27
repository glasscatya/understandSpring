package com.glasscat.spring01ApplocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component02 {
    private static final Logger log = LoggerFactory.getLogger(Component02.class);

    @EventListener
    public void eventHandler(UserRegisteredEvent event) {
        log.debug("接收到事件:{}", event);
        System.out.println(event.getSource());
    }
}
