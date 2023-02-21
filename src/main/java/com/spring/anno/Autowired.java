package com.spring.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})//写在方法
public @interface Autowired {//自动注入,给这个注入一个值
    boolean required() default true;  //传入的值，默认值为tree
}
