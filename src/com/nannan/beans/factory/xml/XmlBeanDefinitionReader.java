package com.nannan.beans.factory.xml;

import com.nannan.beans.factory.PropertyValues;
import com.nannan.beans.factory.config.BeanDefinition;
import com.nannan.beans.factory.config.ConstructorArgumentValue;
import com.nannan.beans.factory.config.ConstructorArgumentValues;
import com.nannan.beans.factory.support.AbstractBeanFactory;
import com.nannan.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//load bean definitions from xml
public class XmlBeanDefinitionReader {
    private final AbstractBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    // load bean definitions from xml config to bean factory
    public void loadBeanDefinitions(Resource<Element> resource){
        while (resource.hasNext()) {
            Element element = resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");

            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);

            // load property definition
            PropertyValues propertyValues = new PropertyValues();
            List<Element> propertyElements = element.elements("property");
            List<String> refs = new ArrayList<>();
            propertyElements.forEach(propertyElement -> {
                String type = propertyElement.attributeValue("type");
                String name = propertyElement.attributeValue("name");
                String basicValue = propertyElement.attributeValue("value");
                String beanReference = propertyElement.attributeValue("ref");

                //figure out this property is from a reference of another bean, or a basic value
                boolean isRef = false;
                String propertyValue = "";
                if(basicValue != null && !Objects.equals(basicValue, "")){
                    propertyValue = basicValue;
                }else if(beanReference != null && !Objects.equals(beanReference, "")){
                    propertyValue = beanReference;
                    isRef = true;
                    refs.add(beanReference);
                }

                propertyValues.addPropertyValue(type, name, propertyValue, isRef);
            });
            beanDefinition.setPropertyValues(propertyValues);
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            //load constructor arguments definition
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            List<Element> constructorElements = element.elements("constructor-arg");
            constructorElements.forEach(constructorElement -> {
                argumentValues.addArgumentValue(new ConstructorArgumentValue(
                    constructorElement.attributeValue("type"),
                        constructorElement.attributeValue("name"),
                        constructorElement.attributeValue("value")));
            });
            beanDefinition.setConstructorArgumentValues(argumentValues);

            this.beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }
}
