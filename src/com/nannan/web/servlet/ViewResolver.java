package com.nannan.web.servlet;

public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
