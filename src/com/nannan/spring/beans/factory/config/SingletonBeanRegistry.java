package com.nannan.spring.beans.factory.config;

import java.util.List;

//每个bean 以单例存储与维护
public interface SingletonBeanRegistry {
    void registerSingletonBean(String beanName, Object singletonBean);

    Object getSingletonBean(String beanName);

    Boolean containsSingletonBean(String beanName);

    List<String> getSingletonNames();
}
