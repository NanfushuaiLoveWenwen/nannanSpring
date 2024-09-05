package com.nannan.spring.beans.factory.config;

import com.nannan.spring.beans.factory.PropertyValues;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

@Data
public class BeanDefinition {
    private final static String SCOPE_SINGLETON = "singleton";
    private final static String SCOPE_PROTOTYPE = "prototype";

    private boolean lazyInit = true;
    //当前bean 依赖的其他的bean的reference
    private String[] dependsOn;

    @NonNull
    private ConstructorArgumentValues constructorArgumentValues;
    @NonNull
    private PropertyValues propertyValues;

    private String initMethodName;
    private volatile Object beanClass;

    private String beanName;
    private String className;
    private String scope = SCOPE_SINGLETON;
    public BeanDefinition(String beanName, String className) {
        this.beanName = beanName;
        this.className = className;
    }

    public boolean isSingleton() {
        return Objects.equals(SCOPE_SINGLETON, this.scope);
    }

    public boolean isPrototype() {
        return Objects.equals(SCOPE_PROTOTYPE, this.scope);
    }
}
