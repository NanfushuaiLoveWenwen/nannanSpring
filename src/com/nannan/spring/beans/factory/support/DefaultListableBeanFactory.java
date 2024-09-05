package com.nannan.spring.beans.factory.support;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.nannan.spring.beans.factory.config.BeanDefinition;
import com.nannan.spring.beans.factory.config.BeanPostProcessor;
import com.nannan.spring.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {


    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return (String[]) this.beanDefinitionNames.toArray();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        //遍历已注册的beanDefinition 返回所需类型的beanName集合
        return this.beanDefinitionNames.stream().filter(beanName -> {
            BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
            Class<? extends BeanDefinition> classToMatch = beanDefinition.getClass();


            //判断当前Class对象所表示的类或接口是否与指定的Class参数所表示的类或接口相同，或者是其超类或超接口。
            //用于检查一个类是否可以从另一个类赋值
            return type.isAssignableFrom(classToMatch);
        }).toArray(String[]::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);

        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for(String beanName : beanNames){
            result.put(beanName, (T) getBean(beanName));
        }
        return result;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {

    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return new String[0];
    }
}
