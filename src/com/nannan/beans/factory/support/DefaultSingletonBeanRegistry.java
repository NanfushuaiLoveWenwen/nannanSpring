package com.nannan.beans.factory.support;

import com.nannan.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected final List<String> beanNames = new ArrayList<>();
    // beanName -> beanInstance 本地缓存
    protected final Map<String, Object> singletonBeans = new ConcurrentHashMap<>(256);
    protected Map<String,Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    @Override
    public void registerSingletonBean(String beanName, Object singletonBean) {
        synchronized (this.singletonBeans) {
            this.singletonBeans.put(beanName, singletonBean);
            this.beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingletonBean(String beanName) {
        return this.singletonBeans.get(beanName);
    }

    @Override
    public Boolean containsSingletonBean(String beanName) {
        return singletonBeans.containsKey(beanName);
    }



    @Override
    public List<String> getSingletonNames() {
        return beanNames;
    }

    protected void removeSingletonBean(String beanName) {
        synchronized (this.singletonBeans) {
            beanNames.remove(beanName);
            singletonBeans.remove(beanName);
        }
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }

        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.get(beanName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<String>(8);
                this.dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<String>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }

    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }
    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[]) dependenciesForBean.toArray();

    }
}
