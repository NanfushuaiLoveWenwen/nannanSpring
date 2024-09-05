package com.nannan.spring.context;

import com.nannan.spring.beans.BeansException;
import com.nannan.spring.beans.factory.BeanFactory;
import com.nannan.spring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.nannan.spring.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.nannan.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.spring.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.spring.beans.factory.support.DefaultListableBeanFactory;
import com.nannan.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.nannan.spring.core.ClassPathXmlResource;

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
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    void initApplicationEventPublisher() {
        this.setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    @Override
    void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("context refreshed........"));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }
}
