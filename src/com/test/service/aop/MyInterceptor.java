package com.test.service.aop;

import com.nannan.aop.MethodInterceptor;
import com.nannan.aop.MethodInvocation;

public class MyInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("method "+invocation.getMethod()+" is called on "+
                invocation.getThis()+" with args "+invocation.getArguments());
        Object ret=invocation.proceed();
        System.out.println("method "+invocation.getMethod()+" returns "+ret);

        return ret;
    }
}
