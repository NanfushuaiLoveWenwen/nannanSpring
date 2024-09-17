package com.nannan.beans.factory.support;

import com.nannan.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
     void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

     void removeBeanDefinition(String beanName);

     BeanDefinition getBeanDefinition(String beanName);

     boolean containsBeanDefinition(String beanName);
}
