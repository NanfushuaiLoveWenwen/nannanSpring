package com.nannan.spring.core.env;

//获取属性
public interface Environment extends PropertyResolver{
    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    boolean acceptsProfiles(String... profiles);
}
