package com.nannan.beans.factory.config;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
