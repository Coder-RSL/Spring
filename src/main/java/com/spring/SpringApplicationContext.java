package com.spring;

public class SpringApplicationContext {
    private Class confifClass;

    public SpringApplicationContext(Class confifClass) {
        this.confifClass = confifClass;
        //解析配置类
        //ComponentScan注解------>扫描路径----->扫描

    }

    public Object getBean(String beanName){
        return null;
    }
}
