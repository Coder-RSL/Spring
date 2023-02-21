package com.zhouyu;

import com.spring.SpringApplicationContext;
import com.zhouyu.service.UserService;


public class Test {//测试Spring源码
    public static void main(String[] args) {
        SpringApplicationContext applicationContext =new SpringApplicationContext(AppConfig.class);//spring的配置 文件

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();

    }
}
