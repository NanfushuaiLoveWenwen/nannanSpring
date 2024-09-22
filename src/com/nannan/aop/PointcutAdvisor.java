package com.nannan.aop;

//支持pointcut的advisor
public interface PointcutAdvisor extends Advisor{
    Pointcut getPointcut();
}
