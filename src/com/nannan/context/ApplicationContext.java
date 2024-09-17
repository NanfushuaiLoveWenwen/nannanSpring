package com.nannan.context;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.ListableBeanFactory;
import com.nannan.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.beans.factory.config.ConfigurableBeanFactory;
import com.nannan.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.core.env.Environment;
import com.nannan.core.env.EnvironmentCapable;

//将表示所有特性的接口 收口在applicationContext接口 不同特性的接口都有各自的实现类
//主流程(applicationContext的实现类) 通过组合的方式依赖于各个特性的实现类

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}

