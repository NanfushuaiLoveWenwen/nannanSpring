package com.nannan.aop;

public class DefaultAopProxyFactory implements AopProxyFactory{
    @Override
    public AopProxy createProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
