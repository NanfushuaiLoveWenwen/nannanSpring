package com.nannan.beans.factory;

//Spring框架中的一个接口，但它定义了一种特殊的Bean，这种Bean可以通过自身的方法来创建其他的Bean。

/**
 * FactoryBean 在以下场景中特别有用：
 * 当Bean的创建过程非常复杂，需要涉及多个步骤和依赖时。
 * 当需要动态地决定创建哪个Bean实例时，例如基于配置或环境条件。
 * 当需要创建一些非典型的Bean，如那些不是通过构造函数直接实例化的对象。
 *
 * 如：实现无侵入式创建动态代理对象
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton(){
        return true;
    }
}
