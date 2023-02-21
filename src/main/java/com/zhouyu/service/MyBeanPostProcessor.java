package com.zhouyu.service;

import com.spring.Interface.BeanPostProcessor;
import com.spring.anno.Component;
import org.springframework.beans.BeansException;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化前");
        if(beanName.equals("userService")){
            ((UserService)bean).setName("Naruto");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化后");
        return bean;
    }
}
