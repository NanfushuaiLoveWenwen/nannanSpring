package com.nannan.web.servlet;

import com.nannan.beans.BeansException;
import com.nannan.web.context.support.AnnotationConfigWebApplicationContext;
import com.nannan.web.context.WebApplicationContext;
import com.nannan.web.context.support.XmlScanComponentHelper;
import com.nannan.web.method.HandlerMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    private List<String> packageNames = new ArrayList<>(); //存储需要扫描的package列表

    private String contextConfigLocation;
    private HandlerAdapter handlerAdapter;
    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    //servlet启动的controller层容器
    private WebApplicationContext webApplicationContext;

    //由listener启动的service层容器
    private WebApplicationContext parentWebApplicationContext;

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
    public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
    public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
    private static final Properties defaultStrategies = null;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //webAC 在listener中已经注册好，由servletContext持有
        this.parentWebApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        //servlet相关配置文件的目录
        contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(contextConfigLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, this.parentWebApplicationContext);
        refresh();
    }

    protected void refresh() {
        //对扫描到的每一个类进行加载与实例化 存储mapping关系
        // initController();
        //初始化URL映射
        initHandlerMappings(webApplicationContext);
        initHandlerAdapter(webApplicationContext);
        initViewResolvers(webApplicationContext);
    }

    protected void initHandlerMappings(WebApplicationContext webApplicationContext){
        try {
            this.handlerMapping = (HandlerMapping) webApplicationContext.getBean(HANDLER_MAPPING_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    protected void initHandlerAdapter(WebApplicationContext webApplicationContext){
        try {
            this.handlerAdapter = (HandlerAdapter) webApplicationContext.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processRequest = request;
        HandlerMethod handlerMethod = null;

        handlerMethod = this.handlerMapping.getHandler(processRequest);
        if(handlerMethod == null){
            return;
        }

        ModelAndView modelAndView = this.handlerAdapter.handle(processRequest, response, handlerMethod);

        render(processRequest, response, modelAndView);
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }


    protected void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
        if (mv == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        String sTarget = mv.getViewName();
        Map<String, Object> modelMap = mv.getModel();
        View view = resolveViewName(sTarget, modelMap, request);
        view.render(modelMap, request, response);

    }

    protected View resolveViewName(String viewName, Map<String, Object> model,
                                   HttpServletRequest request) throws Exception {
        if (this.viewResolver != null) {
            View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

}
