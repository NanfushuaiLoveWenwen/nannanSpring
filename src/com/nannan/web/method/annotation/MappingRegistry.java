package com.nannan.web.method.annotation;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//存储访问的 URL 名称与对应调用方法及 Bean 实例的关系
@Data
public class MappingRegistry {
    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String,Object> mappingObjs = new HashMap<>();
    private Map<String, Method> mappingMethods = new HashMap<>();
    private Map<String,String> mappingMethodNames = new HashMap<>();
    private Map<String,Class<?>> mappingClasses = new HashMap<>();

}
