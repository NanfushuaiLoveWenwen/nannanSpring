package com.nannan.web.context.support;

import com.nannan.beans.BeansException;
import com.nannan.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.nannan.beans.factory.config.BeanDefinition;
import com.nannan.beans.factory.config.BeanFactoryPostProcessor;
import com.nannan.beans.factory.config.ConfigurableListableBeanFactory;
import com.nannan.beans.factory.support.DefaultListableBeanFactory;
import com.nannan.context.*;
import com.nannan.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {
    private WebApplicationContext parentApplicationContext;
    private ServletContext servletContext;
    DefaultListableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AnnotationConfigWebApplicationContext(String fileName) {
        //当 Sevlet 服务器启动时，Listener 会优先启动，读配置文件路径，启动过程中初始化上下文
        // 然后启动 IoC 容器，这个容器通过 refresh() 方法加载所管理的 Bean 对象
        // 这样就实现了 Tomcat 启动的时候同时启动 IoC 容器。
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext){
        this.parentApplicationContext = parentApplicationContext;
        this.servletContext = this.parentApplicationContext.getServletContext();

        URL xmlPath = null;
        try{
            xmlPath = this.getServletContext().getResource(fileName);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        this.beanFactory = new DefaultListableBeanFactory();
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
        loadBeanDefinition(controllerNames);

        try{
            super.refresh();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadBeanDefinition(List<String> controllerNames){
        for(String controller : controllerNames){
            String beanId = controller;
            String beanClassName = controller;

            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }

        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void registerListeners() {
        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            Object bean = null;
            try {
                bean = getBean(bdName);
            } catch (BeansException e1) {
                e1.printStackTrace();
            }

            if (bean instanceof ApplicationListener) {
                this.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
            }
        }
    }

    @Override
    public void initApplicationEventPublisher() {
        this.setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {

    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }
}
