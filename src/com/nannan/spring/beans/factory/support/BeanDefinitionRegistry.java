package com.nannan.spring.beans.factory.support;

import com.nannan.spring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
     void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

     void removeBeanDefinition(String beanName);

     BeanDefinition getBeanDefinition(String beanName);

     boolean containsBeanDefinition(String beanName);
}
