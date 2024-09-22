package com.nannan.aop;

public class NameMatchMethodPointcutAdvisor implements PointcutAdvisor{
    private Advice advice = null;
    private MethodInterceptor methodInterceptor;
    private String mappedName;
    private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

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
        return this.advice;
    }

    public void setAdvice(Advice advice){
        this.advice = advice;

        MethodInterceptor methodInterceptor = null;
        if(advice instanceof BeforeAdvice){
            methodInterceptor = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice)advice);
        }else if(advice instanceof AfterAdvice)
        {
            methodInterceptor = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
        }else if(advice instanceof MethodInterceptor){
            methodInterceptor = (MethodInterceptor)advice;
        }

        setMethodInterceptor(methodInterceptor);
    }
    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
        this.pointcut.setMappedName(this.mappedName);
    }

}
