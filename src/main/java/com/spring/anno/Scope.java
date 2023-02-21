package com.spring.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)//写在类上
public @interface Scope {//是否为单例Bean
    String value() default "";//传入的值，默认值为空
    //实现一个Map创建出单例池
}
