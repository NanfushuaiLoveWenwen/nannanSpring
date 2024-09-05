package com.nannan.spring.beans.factory.config;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.nannan.spring.beans.factory.support.AbstractBeanFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 支持autowire 注入bean
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount(){
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() { return this.beanPostProcessors; }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
       Object result = existingBean;

       for(BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()){
           beanPostProcessor.setBeanFactory(this);
           result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
           if(result == null){
               //不可能为null
               return result;
           }
       }
       return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()){
            result = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if(result == null){
                return result;
            }
        }
        return result;
    }
}
