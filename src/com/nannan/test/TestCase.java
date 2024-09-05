package com.nannan.test;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.context.ClassPathXmlApplicationContext;

public class TestCase {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        AService aservice = (AService)ctx.getBean("aservice");
        aservice.sayHello();
    }
}
