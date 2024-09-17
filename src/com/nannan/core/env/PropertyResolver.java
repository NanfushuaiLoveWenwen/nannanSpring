package com.nannan.core.env;

public interface PropertyResolver {
    boolean containsProperty(String key);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

    <T> T getProperty(String key, Class<T> targetType);

    <T> T getProperty(String key, Class<T> targetType, T defaultValue);

    <T> Class<T> getPropertyAsClass(String key, Class<T> targetType);

    String getRequiredProperty(String key)  throws IllegalAccessException;

    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalAccessException;

    String resolvePlaceHolders(String text) throws IllegalArgumentException;
}
