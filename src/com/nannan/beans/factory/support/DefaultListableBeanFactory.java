package com.nannan.beans.factory.support;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.nannan.beans.factory.config.BeanDefinition;
import com.nannan.beans.factory.config.BeanPostProcessor;
import com.nannan.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    public ConfigurableListableBeanFactory parentBeanFactory;
    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return (String[]) this.beanDefinitionNames.toArray(new String[this.beanDefinitionNames.size()]);
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

    public void setParent(ConfigurableListableBeanFactory beanFactory){
        this.parentBeanFactory = beanFactory;
    }


    @Override
    public Object getBean(String beanName) throws BeansException{
        Object result = super.getBean(beanName);
        if (result == null) {
            result = this.parentBeanFactory.getBean(beanName);
        }

        return result;
    }
}
