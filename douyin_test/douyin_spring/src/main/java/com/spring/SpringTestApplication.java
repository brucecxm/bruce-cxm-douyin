package com.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class SpringTestApplication {
    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(beanFactory);

        reader.loadBeanDefinitions("beans.xml");

        testbean testbeanoone=(testbean) beanFactory.getBean("testbean");

        System.out.println(testbeanoone);




    }
}
