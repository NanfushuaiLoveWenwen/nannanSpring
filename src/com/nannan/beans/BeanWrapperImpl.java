package com.nannan.beans;

import com.nannan.beans.factory.PropertyValues;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject; //目标对象
    Class clz;
    PropertyValues pvs; //参数值

    public BeanWrapperImpl(Object object) {
        registerDefaultEditors(); //不同数据类型的参数转换器editor
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    } //绑定参数值

    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    //绑定具体某个参数
    public void setPropertyValue(PropertyValue pv) {
        //拿到参数处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        //找到对该参数类型的editor
        PropertyEditor pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        //设置参数值
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }


    //一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                this.writeMethod = clz.getDeclaredMethod("set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1),propertyClz);
                this.readMethod = clz.getDeclaredMethod("get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);

            try {
                result =  readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;

        }

        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
}