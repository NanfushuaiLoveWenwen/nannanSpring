package com.nannan.aop;

public class DefaultAdvisor implements Advisor{
    private MethodInterceptor methodInterceptor;

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Advice getAdvice() {
        return this.methodInterceptor;
    }
}
