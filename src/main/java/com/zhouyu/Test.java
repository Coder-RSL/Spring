package com.zhouyu;

import com.spring.SpringApplicationContext;


public class Test {//测试Spring源码
    public static void main(String[] args) {
        SpringApplicationContext applicationContext =new SpringApplicationContext(AppConfig.class);//spring的配置 文件

        Object userService = applicationContext.getBean("userService");//返回一个具体的对象

    }
}