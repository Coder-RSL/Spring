package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class SpringApplicationContext {
    private Class confifClass;

    public SpringApplicationContext(Class confifClass){
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

        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                //把我们得到的字符串f的路径，转换为我们需要的字符串
                String absolutePath = f.getAbsolutePath();
                if(absolutePath.endsWith(".class")){
                    String classNmae = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));

                    classNmae = classNmae.replace("\\", ".");


                    try {
                        Class<?> aClass = classLoader.loadClass(classNmae);
                        if(aClass.isAnnotationPresent(Component.class)){//isAnnotationPresent用于判断一个类、方法、字段等是否被某个特定注解所修饰。
                            //表示当前这个类是一个Bean
                            //创建一个Bean对象

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Object getBean(String beanName){
        return null;
    }
}
