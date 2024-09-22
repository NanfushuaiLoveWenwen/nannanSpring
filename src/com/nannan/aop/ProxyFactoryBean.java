package com.nannan.aop;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.BeanFactory;
import com.nannan.beans.factory.BeanFactoryAware;
import com.nannan.beans.factory.FactoryBean;
import com.nannan.utils.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String targetName;
    private Object target;
    private final ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
    private Object singletonInstance;

    private String interceptorName;
    private PointcutAdvisor advisor;

    public ProxyFactoryBean(){
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }
    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }
    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
    public Object getTarget() {
        return target;
    }
    public void setTarget(Object target) {
        this.target = target;
    }

    protected AopProxy createAopProxy(){
        System.out.println("----------createAopProxy for :"+target+"--------");
        return getAopProxyFactory().createProxy(target, this.advisor);
    }
    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized Object getSingletonInstance(){
        if(this.singletonInstance == null){
            this.singletonInstance = getProxy(createAopProxy());
        }
        System.out.println("----------return proxy for :"+this.singletonInstance+"--------"+this.singletonInstance.getClass());
        return this.singletonInstance;
    }

    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    private synchronized void initializeAdvisor(){
        try {
            Object advice = this.beanFactory.getBean(this.interceptorName);
            this.advisor = (PointcutAdvisor) advice;
        }catch (BeansException e){
            e.printStackTrace();
        }
    }
}
