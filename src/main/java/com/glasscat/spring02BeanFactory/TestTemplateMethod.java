package com.glasscat.spring02BeanFactory;

import java.util.ArrayList;
import java.util.List;

public class TestTemplateMethod {
    public static void main(String[] args) {
        MyBeanFactory myBeanFactory = new MyBeanFactory();
        myBeanFactory.addBeanPostProcess(bean -> System.out.println(bean + "增加@Autowired解析"));
        Object bean = myBeanFactory.getBean();
    }

    static class MyBeanFactory {
        public Object getBean() {
            Object bean = new Object();
            System.out.println("创建" + bean);
            System.out.println("注入" + bean);
            for (BeanPostProcess beanPostProcess : beanPostProcessList) {
                beanPostProcess.inject(bean);
            }
            System.out.println("初始化" + bean);
            return bean;
        }

        private final List<BeanPostProcess> beanPostProcessList = new ArrayList<>();

        public void addBeanPostProcess(BeanPostProcess beanPostProcess) {
            beanPostProcessList.add(beanPostProcess);
        }
    }

    static interface BeanPostProcess {
        public void inject(Object bean);
    }
}
