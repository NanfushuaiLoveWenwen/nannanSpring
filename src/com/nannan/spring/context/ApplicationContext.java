package com.nannan.spring.context;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.beans.factory.ListableBeanFactory;
import com.nannan.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.spring.beans.factory.config.ConfigurableBeanFactory;
import com.nannan.spring.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.spring.core.env.Environment;
import com.nannan.spring.core.env.EnvironmentCapable;

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

