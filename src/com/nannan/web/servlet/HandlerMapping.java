package com.nannan.web.servlet;

import com.nannan.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

//管理所有url 和具体handler的mapping关系
public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
