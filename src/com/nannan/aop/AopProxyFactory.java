package com.nannan.aop;

public interface AopProxyFactory {
    AopProxy createProxy(Object target, PointcutAdvisor advisor);
}
