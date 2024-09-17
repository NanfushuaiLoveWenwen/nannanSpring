package com.nannan.beans.factory.annotation;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.BeanFactory;
import com.nannan.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clazz = result.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //遍历所有字段，看看哪个字段有Autowired注解
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object autowiredBean = this.beanFactory.getBean(fieldName);
                field.setAccessible(true);
                try {
                    field.set(bean, autowiredBean);
                    System.out.println("autowire " + fieldName + "for bean" + beanName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
