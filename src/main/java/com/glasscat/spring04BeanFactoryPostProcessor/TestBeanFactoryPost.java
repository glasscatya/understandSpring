package com.glasscat.spring04BeanFactoryPostProcessor;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

public class TestBeanFactoryPost {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);

        //添加一个工厂后处理器：ConfigurationClassPostProcessor
        // 他会去扫描Bean身上的@ComponentScan和@Bean注解 然后去进一步的解析
        //context.registerBean(ConfigurationClassPostProcessor.class);
        //context.registerBean(MapperScannerConfigurer.class, bd -> {
        //    bd.getPropertyValues()
        //      .add("basePackage", "com.glasscat.spring04BeanFactoryPostProcessor.mapper");
        //});
        //手工实现注解扫描
        //ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        //if (componentScan != null) {
        //    //拿到了componentScan想要扫描的包们(因为可能是个数组
        //    for (String name : componentScan.basePackages()) {
        //        //将包路径 更改成 OS路径
        //        String path = "classpath*:" + name.replace(".", "/") + "/**/*.class";
        //        //通过ApplicationContext拓展的读取资源能力，去读取到路径下的类
        //        Resource[] resources = context.getResources(path);
        //        //获取.class文件的类信息 如类名，类是否有注解
        //        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        //        AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        //        for (Resource resource : resources) {
        //            MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
        //            //获取到类的注解信息
        //            AnnotationMetadata readerAnnotationMetadata = metadataReader.getAnnotationMetadata();
        //            //如果一个类包含目标注解或目标注解的派生类
        //            if (readerAnnotationMetadata.hasAnnotation(Component.class.getName())
        //                    || readerAnnotationMetadata.hasMetaAnnotation(Component.class.getName())) {
        //                AbstractBeanDefinition beanDefinition =
        //                        BeanDefinitionBuilder.genericBeanDefinition(metadataReader.getClassMetadata().getClassName())
        //                                             .getBeanDefinition();
        //                DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        //                String bdname = beanNameGenerator.generateBeanName(beanDefinition, beanFactory);
        //                beanFactory.registerBeanDefinition(bdname ,beanDefinition);
        //            }
        //        }
        //    }
        //}
        /**
        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = readerFactory.getMetadataReader(new ClassPathResource("com/glasscat/spring04BeanFactoryPostProcessor/config.class"));
        Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());

        for (MethodMetadata method : methods) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
            beanDefinitionBuilder.setFactoryMethodOnBean(method.getMethodName(), "config");
            beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            context.registerBeanDefinition(method.getMethodName(), beanDefinition);
        }
        */

        context.registerBean(AtBeanPostProcessor.class);
        //context.registerBean(ComponentScanPostProcessor.class);
        context.refresh(); //初始化容器，他会主动的调用所有工厂后处理器，bean后处理器
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        context.close();
    }
}
