package com.zhouyu.service;

import com.spring.Interface.BeanNameAware;
import com.spring.anno.Autowired;
import com.spring.anno.Component;
import com.spring.Interface.InitializingBean;

//@Component("userService")
@Component("userService")//spring中会有一个规则
//@Scope("prototype")//原型Bean
public class UserService implements InitializingBean, BeanNameAware {

    @Autowired
    private OrderService orderService;

    @Autowired
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    private String beanName;//想要Spring给beanName属性赋值，Autowired做不到
    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName=name;
        System.out.println("BeanNameAware接口方法");
        System.out.println("修改beanName:"+beanName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Spring提供的一种初始化的操作
        System.out.println("InitializingBean接口");
        System.out.println("xxx初始化操作");
    }
}
