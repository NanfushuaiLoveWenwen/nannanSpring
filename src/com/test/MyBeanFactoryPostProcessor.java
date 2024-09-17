package com.test;


import com.nannan.beans.BeansException;
import com.nannan.beans.factory.BeanFactory;
import com.nannan.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println(".........MyBeanFactoryPostProcessor...........");
		
	}

}
