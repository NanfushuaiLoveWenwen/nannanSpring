package com.nannan.web.context;

import com.nannan.context.ApplicationContext;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName()+".ROOT";

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);
}
