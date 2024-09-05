package com.nannan.spring.beans.factory.config;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
