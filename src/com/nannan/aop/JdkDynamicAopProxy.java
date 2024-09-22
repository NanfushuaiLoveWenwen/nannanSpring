package com.nannan.aop;

import com.test.service.aop.Action;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final Object target;
    private final PointcutAdvisor advisor;

    public JdkDynamicAopProxy(Object target, PointcutAdvisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        Class<?>[] interfaces = this.target.getClass().getInterfaces();
        ClassLoader classLoader = this.target.getClass().getClassLoader();

        System.out.println("----------Proxy new proxy instance for  ---------" + target);
        System.out.println("----------Proxy new proxy instance  classloader ---------" + classLoader);
        System.out.println("----------Proxy new proxy instance  interfaces  ---------" + Arrays.toString(interfaces));

        Object proxyObj = Proxy.newProxyInstance(classLoader, interfaces, this);
        System.out.println("----------Proxy new proxy instance created  ---------" + proxyObj);
        return proxyObj;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = Optional.ofNullable(target).map(Object::getClass).orElse(null);

        if (this.advisor.getPointcut().getMethodMatcher().matches(method, targetClass)) {
            MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
            ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return methodInterceptor.invoke(reflectiveMethodInvocation);
        } else {
            return method.invoke(target, args);
        }
    }
}
