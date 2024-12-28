package com.glasscat.spring03BeanPostProcessor;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        //添加工厂@Value解析功能
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //beanFactory.addEmbeddedValueResolver(Stan);

        Bean1 bean1 = new Bean1();
        //拿到自动注入注解Bean后处理器
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        //将后处理器与beanfactory绑定，因为@autowried解析过程中，所需要的Bean都在容器里。我们需要提供给一个容器
        processor.setBeanFactory(beanFactory);
        //System.out.println(bean1);
        //processor.postProcessProperties(null, bean1, "这是一个测试bean");
        //System.out.println(bean1);

        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        InjectionMetadata injectionMetadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(injectionMetadata);
        injectionMetadata.inject(bean1, "bean1", null);
        System.out.println(bean1);
    }
}
