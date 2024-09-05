package com.nannan.spring.beans.factory.support;


import com.nannan.spring.beans.*;
import com.nannan.spring.beans.factory.BeanFactory;
import com.nannan.spring.beans.factory.config.ConfigurableBeanFactory;
import com.nannan.spring.beans.factory.config.ConstructorArgumentValue;
import com.nannan.spring.beans.factory.config.ConstructorArgumentValues;
import com.nannan.spring.beans.factory.config.BeanDefinition;
import com.nannan.spring.beans.factory.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    //beanName -> beanDefinition
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected final List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }

    //对bean definition中的所有bean 调用 getBean方法
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //懒加载获取bean实例
    @Override
    public Object getBean(String beanName) throws BeansException {
        //0 从容器拿bean
        Object singletonBean = this.getSingletonBean(beanName);
        if (singletonBean == null) {
            //1 容器没有，从early毛坯实例容器中拿,并直接返回
            singletonBean = this.earlySingletonObjects.get(beanName);
            if (singletonBean != null) return singletonBean;

            //2 毛坯容器没有 从bean definition中加载 创建instance 并注册
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            singletonBean = createBean(beanDefinition);
            //3 将create的单例bean 注册到本地缓存中
            this.registerSingletonBean(beanName, singletonBean);

            //4 bean post processor 在创建bean 之后执行
            //4.1 post processor before initialization
            applyBeanPostProcessorsBeforeInitialization(singletonBean, beanName);

            //4.2 init method
            if(beanDefinition.getInitMethodName() != null && !beanDefinition.getInitMethodName().equals("")){
                invokeInitMethod(beanDefinition, singletonBean);
            }
            //4.3 post processor after initialization
            applyBeanPostProcessorsAfterInitialization(singletonBean, beanName);
        }
        return singletonBean;
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
        this.beanDefinitionNames.add(beanName);

        //如果不是懒加载 就在注册bean definition 的时候创建bean实例
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionMap.remove(beanName);
        this.beanDefinitionNames.remove(beanName);
        this.removeSingletonBean(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    public boolean containsBean(String beanName) {
        return this.containsSingletonBean(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return this.beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return this.beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitionMap.get(beanName).getClass();
    }

    public void registerBean(String beanName, Object beanInstance) {
        this.registerSingletonBean(beanName, beanInstance);
    }

    //create a bean instance from bean definition
    public Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        //1 创建毛坯实例
        Object obj = doCreateBean(beanDefinition);
        //2 注册毛坯实例
        this.earlySingletonObjects.put(beanDefinition.getBeanName(), obj);

        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //3 set具体属性 放在最后
        populateBean(beanDefinition, clazz, obj);
        return obj;
    }

    //基于构造方法创建毛坯实例 还未set具体属性
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazz;
        Constructor<?> constructor;
        // 处理构造器参数 一次性set多个
        try {
            clazz = Class.forName(beanDefinition.getClassName());
            ConstructorArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];

                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue indexedArgumentValue = argumentValues.getIndexedArgumentValue(i);
                    if (Objects.equals("String", indexedArgumentValue.getType()) || Objects.equals("java.lang.String", indexedArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = indexedArgumentValue.getValue();
                    } else if (Objects.equals("Integer", indexedArgumentValue.getType()) || Objects.equals("java.lang.Integer", indexedArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) indexedArgumentValue.getValue());
                    } else if (Objects.equals("int", indexedArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) indexedArgumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = indexedArgumentValue.getValue();
                    }
                }

                constructor = clazz.getConstructor(paramTypes);
                return constructor.newInstance(paramValues);
            } else {
                return clazz.getConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
    private void populateBean(BeanDefinition bd, Class<?> clz, Object obj) {
        handleProperties(bd, clz, obj);
    }

    //load property config from bean definition
    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        //处理property参数 一个一个set 所以paramTypes长度为1
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.getSize(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);

                String type = propertyValue.getType();
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                boolean isRef = propertyValue.isRef();

                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];

                if (isRef) {
                    // is ref property, should create ref bean before
                    try {
                        paramTypes[0] = Class.forName(type);
                        //调用getBean方法 通过bean ref的bean name 获取instance
                        paramValues[0] = getBean((String) value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    if (Objects.equals("String", type) || Objects.equals("java.lang.String", type)) {
                        paramTypes[0] = String.class;
                    } else if (Objects.equals("Integer", type) || Objects.equals("java.lang.Integer", type)) {
                        paramTypes[0] = Integer.class;
                    } else if (Objects.equals("int", type)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = value;
                }



                //获取set方法
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                try {
                    Method method = clazz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void invokeInitMethod(BeanDefinition bd, Object obj) {
        Class<?> clz = obj.getClass();
        try {
            Method method = clz.getMethod(bd.getInitMethodName());
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
