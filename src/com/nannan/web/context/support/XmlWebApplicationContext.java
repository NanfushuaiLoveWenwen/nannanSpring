package com.nannan.web.context.support;

import com.nannan.context.ClassPathXmlApplicationContext;
import com.nannan.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {
    private ServletContext servletContext;

    // 由servletContextListener负责初始化
    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
