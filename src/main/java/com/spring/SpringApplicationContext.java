package com.spring;

import com.spring.Interface.BeanNameAware;
import com.spring.Interface.BeanPostProcessor;
import com.spring.Interface.InitializingBean;
import com.spring.anno.*;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.DoubleStream;

public class SpringApplicationContext {
    private Class configClass;

    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();//单例池
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();//主要是存储整个系统Bean的定义
    private List<BeanPostProcessor> beanPostProcessorList =new ArrayList<>();

    public SpringApplicationContext(Class configClass){
        this.configClass = configClass;

        //解析配置类
        //ComponentScan注解------>扫描路径----->扫描--->Beandefinition--->BeanDefinitionMap
        scan(configClass);
        ConcurrentHashMap.KeySetView<String, BeanDefinition> keys = beanDefinitionMap.keySet();
        for(String beanName: keys){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){//找出单例bean
                Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);//存入单例池中
            }
        }

    }

    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();


        try {

            Object instance = clazz.getDeclaredConstructor().newInstance();//获取实例
            //依赖注入
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(declaredField.isAnnotationPresent(Autowired.class)){
                    //基于属性的信息
                    String name = declaredField.getName();
                    Object bean = getBean(name);//从单例池种寻找

                    declaredField.setAccessible(true);//访问控制机制，强制访问私有成员（字段、方法、构造函数等），并修改其值。
                    declaredField.set(instance,bean);
                }
            }

            //Aware回调
            if(instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);//强制将BeanName传入
            }

            //BeanPostProcessor初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance  = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            //初始化
            if(instance instanceof InitializingBean){
                try{
                    ((InitializingBean)instance).afterPropertiesSet();//调用初始化方法，不会管你里面有什么
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //BeanPostProcessor初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance  = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void scan(Class confifClass) {
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
                        Class<?> clazz = classLoader.loadClass(classNmae);
                        if(clazz.isAnnotationPresent(Component.class)){//isAnnotationPresent用于判断一个类、方法、字段等是否被某个特定注解所修饰。
                            //表示当前这个类是一个Bean
                            //创建一个Bean对象

                            //解析类，判断当前的Bean是单例Bean还是prototype 的bean

                            //BeanPostProcessor处理
                            boolean assignableFrom = BeanPostProcessor.class.isAssignableFrom(clazz);
                            if(assignableFrom){
                                BeanPostProcessor instance =(BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }

                            //BeanDefinition
                            Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            String baenName =componentAnnotation.value();

                            BeanDefinition beanDefinition =new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            }
                            else{
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(baenName,beanDefinition);//Spring中Bean的定义

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Object getBean(String beanName){
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            }
            else{
                //创建一个Bean对象
                Object bean = createBean(beanName,beanDefinition);
                return bean;//非单例则创建
            }
        }else{
            //不存在对应的Bean
            System.out.println(beanName+"不存在");
            throw new NullPointerException();
        }
    }
}
