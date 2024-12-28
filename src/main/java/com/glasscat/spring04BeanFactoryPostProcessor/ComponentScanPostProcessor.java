package com.glasscat.spring04BeanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (componentScan != null) {
                //拿到了componentScan想要扫描的包们(因为可能是个数组
                for (String name : componentScan.basePackages()) {
                    //将包路径 更改成 OS路径
                    String path = "classpath*:" + name.replace(".", "/") + "/**/*.class";
                    //通过ApplicationContext拓展的读取资源能力，去读取到路径下的类
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    //获取.class文件的类信息 如类名，类是否有注解
                    CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
                    AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
                    for (Resource resource : resources) {
                        //getMetadataReader是可以直接读取.class二进制文件，不走类的加载机制，而且效率也比反射更高
                        MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                        //获取到类的注解信息
                        AnnotationMetadata readerAnnotationMetadata = metadataReader.getAnnotationMetadata();
                        //如果一个类包含目标注解或目标注解的派生类
                        if (readerAnnotationMetadata.hasAnnotation(Component.class.getName())
                                || readerAnnotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            AbstractBeanDefinition beanDefinition =
                                    BeanDefinitionBuilder.genericBeanDefinition(metadataReader.getClassMetadata().getClassName())
                                                         .getBeanDefinition();
                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
                                //生成一个在beanFactory中独一无二的名字，确保不会出现冲突 所以要把容器给传进去，因为要确保容器内的唯一性
                                String bdname = beanNameGenerator.generateBeanName(beanDefinition, beanFactory);
                                //真正注册进入容器
                                beanFactory.registerBeanDefinition(bdname ,beanDefinition);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
