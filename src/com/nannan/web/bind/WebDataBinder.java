package com.nannan.web.bind;

import com.nannan.beans.BeanWrapperImpl;
import com.nannan.beans.PropertyEditor;
import com.nannan.beans.factory.PropertyValues;
import com.nannan.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//将Request请求内的字符串参数转换成不同类型的参数
public class WebDataBinder {

    private Object target;
    private Class<?> clazz;

    private String objectName;

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clazz = target.getClass();
    }

    //核心绑定方法 将request的参数值绑定到target对象的属性上
    public void bind(HttpServletRequest request) {
        PropertyValues propertyValues = assignParameters(request);
        addBindValues(propertyValues, request);

    }

    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }

    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }

    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }

    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
    }
}
