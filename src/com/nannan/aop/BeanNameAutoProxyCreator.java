package com.nannan.aop;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.BeanFactory;
import com.nannan.beans.factory.config.BeanPostProcessor;
import com.nannan.utils.PatternMatchUtils;

import java.util.PrimitiveIterator;

//在所有bean加载好之后执行
//对满足匹配规则的bean 创建factoryBean
public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    String pattern;
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    private String interceptorName;
    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator(){
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("try to create proxy for: " + beanName);
        if(isMatch(beanName, this.pattern)){
            System.out.println(beanName + "bean name matched," + this.pattern + " create proxy for "+bean);
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            return proxyFactoryBean;
        }else{
            return bean;
        }
    }

    protected AopProxy createAopProxy(Object target){
        return this.aopProxyFactory.createProxy(target, this.advisor);
    }

    protected Object getProxy(AopProxy aopProxy){
        return aopProxy.getProxy();
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    protected boolean isMatch(String beanName, String mappingName){
        System.out.println("match? " + beanName + ":"+ mappingName);
        return PatternMatchUtils.simpleMatch(mappingName, beanName);
    }
}