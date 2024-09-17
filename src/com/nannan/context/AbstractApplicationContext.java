package com.nannan.context;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.beans.factory.config.BeanPostProcessor;
import com.nannan.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext implements ApplicationContext{
    private Environment environment;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startUpDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();
    private ApplicationEventPublisher publisher;


    /**
     * refresh chain
     */

    //注册监听者
    public abstract void registerListeners();
    //初始化事件发布器
    public abstract void initApplicationEventPublisher();
    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);
    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);
    public abstract void onRefresh();
    public abstract void finishRefresh();

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public long getStartupDate() {
        return this.startUpDate;
    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        postProcessBeanFactory(getBeanFactory());

        registerBeanPostProcessors(getBeanFactory());

        initApplicationEventPublisher();

        onRefresh();

        registerListeners();

        finishRefresh();
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return this.active.get();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    //从 bean definition 中查询复合type的beanName
    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    //根据beanName 查出对应的实例
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        this.getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return getBeanFactory().getDependenciesForBean(beanName);
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object resultObj = getBeanFactory().getBean(beanName);
        if (resultObj instanceof ApplicationContextAware) {
            ((ApplicationContextAware) resultObj).setApplicationContext(this);
        }
        return resultObj;
    }

    @Override
    public boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return getBeanFactory().isSingleton(beanName);
    }

    @Override
    public boolean isPrototype(String beanName) {
        return getBeanFactory().isPrototype(beanName);
    }

    @Override
    public Class<?> getType(String beanName) {
        return getBeanFactory().getType(beanName);
    }

    @Override
    public void registerSingletonBean(String beanName, Object singletonBean) {
        this.getBeanFactory().registerSingletonBean(beanName, singletonBean);
    }

    @Override
    public Object getSingletonBean(String beanName) {
        return this.getBeanFactory().getSingletonBean(beanName);
    }

    @Override
    public Boolean containsSingletonBean(String beanName) {
        return this.getBeanFactory().containsSingletonBean(beanName);
    }

    @Override
    public List<String> getSingletonNames() {
        return this.getBeanFactory().getSingletonNames();
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return publisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
