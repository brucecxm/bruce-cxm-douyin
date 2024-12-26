package com.bruce;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;

@SpringBootApplication
public class DouyinSpringApplication {
    private static Map<String, Object> map;

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

    ConfigurableApplicationContext context= SpringApplication.run(DouyinSpringApplication.class);


    context.getBean("brucechen");


        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String,Object> map = (Map<String,Object>)singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(entry -> entry.getKey().startsWith("brucechen"))
                .forEach(entry->{System.out.println(entry.getKey() + "=" + entry.getValue());
                });
    }

}
