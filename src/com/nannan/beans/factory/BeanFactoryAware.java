package com.nannan.beans.factory;

/**
 * 允许一个bean在其初始化时感知到其所在的BeanFactory。
 * 这个接口主要用于获取加载当前Bean的工厂（BeanFactory）的引用，
 * 使得Bean能够在运行时获取到关于自身所在工厂的信息
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory);
}
