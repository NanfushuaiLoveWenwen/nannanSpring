package com.nannan.spring.beans.factory;

import com.nannan.spring.beans.BeansException;

public interface BeanFactory {

    //获取bean实例
    Object getBean(String beanName) throws BeansException;

    boolean containsBean(String beanName);

    boolean isSingleton(String beanName);

    boolean isPrototype(String beanName);
    
    Class<?> getType(String beanName);
}
