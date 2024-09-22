package com.test.service.aop;

import com.nannan.aop.BeforeAdvice;
import com.nannan.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("----------my interceptor before method call----------");

    }
}
