package com.nannan.aop;

import java.lang.reflect.Method;

//看method是否符合某种规则 进行匹配
public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
