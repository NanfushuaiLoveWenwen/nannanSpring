package com.nannan.web.method.annotation;

import com.nannan.beans.BeansException;
import com.nannan.context.ApplicationContext;
import com.nannan.context.ApplicationContextAware;
import com.nannan.context.ApplicationEvent;
import com.nannan.web.bind.annotation.RequestMapping;
import com.nannan.web.context.WebApplicationContext;
import com.nannan.web.method.HandlerMethod;
import com.nannan.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware {

    protected ApplicationContext applicationContext;
    private MappingRegistry mappingRegistry = null;

    //根据url返回对应的调用方法与对象
    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        if (this.mappingRegistry == null) {
            this.mappingRegistry = new MappingRegistry();
            initMapping();
        }
         String servletPath = request.getServletPath();

        if(!this.mappingRegistry.getUrlMappingNames().contains(servletPath)){
            return null;
        }

        //
        Method method = this.mappingRegistry.getMappingMethods().get(servletPath);
        Object object = this.mappingRegistry.getMappingObjs().get(servletPath);
        Class<?> clz = this.mappingRegistry.getMappingClasses().get(servletPath);
        String methodName = this.mappingRegistry.getMappingMethodNames().get(servletPath);
        //从mappingRegistry中拿到url对应的方法和对象，打包返回
        return new HandlerMethod(method, object, clz, methodName);
    }

    protected void initMapping(){
        Class<?> clazz = null;
        Object object = null;

        String[] controllerNames = this.applicationContext.getBeanDefinitionNames();

        //扫描子容器中的所有bean(controller层) 初始化到mappingRegistry中
        for(String controllerName : controllerNames){
            try{
                clazz = Class.forName(controllerName);
                object = this.applicationContext.getBean(controllerName);
            }catch (Exception e){
                e.printStackTrace();
            }

            Method[] declaredMethods = clazz.getDeclaredMethods();
            if(declaredMethods!=null){
                for(Method method : declaredMethods){
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if(isRequestMapping){
                        String methodName = method.getName();
                        String urlMapping = method.getAnnotation(RequestMapping.class).value();
                        this.mappingRegistry.getUrlMappingNames().add(urlMapping);
                        this.mappingRegistry.getMappingObjs().put(urlMapping, object);
                        this.mappingRegistry.getMappingMethods().put(urlMapping, method);
                        this.mappingRegistry.getMappingMethodNames().put(urlMapping, methodName);
                        this.mappingRegistry.getMappingClasses().put(urlMapping, clazz);
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
