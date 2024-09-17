package com.test;

import com.nannan.beans.BeansException;
import com.nannan.context.ClassPathXmlApplicationContext;
import com.test.service.AService;

public class TestCase {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        AService aservice = (AService)ctx.getBean("aservice");
        aservice.sayHello();
    }
}
