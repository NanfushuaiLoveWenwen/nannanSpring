package com.nannan.aop;

public interface Advisor {
    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

    Advice getAdvice();
}
