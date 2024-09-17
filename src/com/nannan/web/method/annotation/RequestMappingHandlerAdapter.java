package com.nannan.web.method.annotation;

import com.nannan.beans.BeansException;
import com.nannan.context.ApplicationContext;
import com.nannan.context.ApplicationContextAware;
import com.nannan.http.converter.HttpMessageConverter;
import com.nannan.web.bind.annotation.ResponseBody;
import com.nannan.web.method.HandlerMethod;
import com.nannan.web.bind.support.WebBindingInitializer;
import com.nannan.web.bind.WebDataBinder;
import com.nannan.web.bind.support.WebDataBinderFactory;
import com.nannan.web.servlet.HandlerAdapter;
import com.nannan.web.servlet.ModelAndView;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Data
public class RequestMappingHandlerAdapter implements HandlerAdapter, ApplicationContextAware {
    protected ApplicationContext applicationContext;
    private WebBindingInitializer webBindingInitializer = null;
    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter() {

    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handlerInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handlerInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {

        try{
            return invokeHandlerMethod(request, response, handlerMethod);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                       HttpServletResponse response,
                                       HandlerMethod handlerMethod) throws Exception {


        WebDataBinderFactory binderFactory = new WebDataBinderFactory();

        //存储调用方法所有的参数
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];

        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            if (methodParameter.getType()!=HttpServletRequest.class && methodParameter.getType()!=HttpServletResponse.class) {
                Object methodParamObj = methodParameter.getType().newInstance();
                WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
                webBindingInitializer.initBinder(wdb);
                wdb.bind(request);
                methodParamObjs[i] = methodParamObj;
            }
            else if (methodParameter.getType()==HttpServletRequest.class) {
                methodParamObjs[i] = request;
            }
            else if (methodParameter.getType()==HttpServletResponse.class) {
                methodParamObjs[i] = response;
            }
            i++;
        }

        Method invocableMethod = handlerMethod.getMethod();
        Object bean = handlerMethod.getBean();
        Object returnObj = invocableMethod.invoke(bean, methodParamObjs);
        Class<?> returnType = invocableMethod.getReturnType();

        ModelAndView modelAndView = null;

        //如果是ResponseBody注解，仅仅返回值，则转换数据格式后直接写到response 此时ModeAndView为null
        if(invocableMethod.isAnnotationPresent(ResponseBody.class)){
            this.messageConverter.write(returnObj, response);
        }else{
            //没有ResponseBody注解 返回前端页面
            if(returnObj instanceof ModelAndView){
                modelAndView = (ModelAndView) returnObj;
            }else if(returnObj instanceof String){
                //字符串也作为ModeAndView返回
                String strTarget = (String)returnObj;
                modelAndView = new ModelAndView();
                modelAndView.setViewName(strTarget);
            }
        }

        return modelAndView;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }
}
