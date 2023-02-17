package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class SpringApplicationContext {
    private Class confifClass;

    public SpringApplicationContext(Class confifClass) {
        this.confifClass = confifClass;

        //解析配置类
        //ComponentScan注解------>扫描路径----->扫描
        //只探索Spring提供的注解
        ComponentScan componentScanAnnotation = (ComponentScan) confifClass.getDeclaredAnnotation(ComponentScan.class);//拿到某一个注解
        String path =componentScanAnnotation.value();//获得扫描路径
        System.out.println(path);

        // 扫描
        // Bootstrap----->jre/lib
        // Ext----------->jre/txt/lib
        // APP----------->classpath

        ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();//app的classloader
        URL resource = classLoader.getResource("com/zhouyu/service");//通过类加载器获取资源
//相对路径（classpath） target里的文件

        File file =new File(resource.getFile());
        System.out.println(file);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                System.out.println(f);
            }
        }
    }

    public Object getBean(String beanName){
        return null;
    }
}
