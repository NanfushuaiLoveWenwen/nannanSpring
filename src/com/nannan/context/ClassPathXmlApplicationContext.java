package com.nannan.context;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.nannan.beans.factory.config.BeanDefinition;
import com.nannan.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.beans.factory.config.BeanPostProcessor;
import com.nannan.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.beans.factory.support.DefaultListableBeanFactory;
import com.nannan.beans.factory.xml.XmlBeanDefinitionReader;
import com.nannan.core.ClassPathXmlResource;

import java.util.ArrayList;
import java.util.List;

//主流程
public class ClassPathXmlApplicationContext extends AbstractApplicationContext{

    private final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    public ClassPathXmlApplicationContext(String fileName){
        this(fileName, true);
    }
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);

        if(isRefresh){
            try{
                refresh();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {

    }

    @Override
    public void registerListeners() {
        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            Object bean = null;
            try {
                bean = getBean(bdName);
            } catch (BeansException e1) {
                e1.printStackTrace();
            }

            if (bean instanceof ApplicationListener) {
                this.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
            }
        }

    }

    @Override
    public void initApplicationEventPublisher() {
        this.setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            BeanDefinition bd = this.beanFactory.getBeanDefinition(bdName);
            String clzName = bd.getClassName();
            Class<?> clz = null;
            try {
                clz = Class.forName(clzName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            if (BeanPostProcessor.class.isAssignableFrom(clz)) {
                try {
                    this.beanFactory.addBeanPostProcessor((BeanPostProcessor) clz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }
}
