package com.nannan.beans.factory;

import com.nannan.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertyValues {
    private List<PropertyValue> propertyValueList = new ArrayList<>();

    public PropertyValues() {
    }
    public PropertyValues(Map<String, Object> map) {
        this.propertyValueList = new ArrayList<PropertyValue>(10);
        for (Map.Entry<String,Object> e: map.entrySet()) {
            PropertyValue pv = new PropertyValue(e.getKey(),e.getValue());
            this.propertyValueList.add(pv);
        }
    }



    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public int getSize() {
        return propertyValueList.size();
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.add(propertyValue);
    }

    public void addPropertyValue(String type, String name, Object value, boolean isRef) {
        addPropertyValue(new PropertyValue(type, name, value, isRef));
    }

    public void removePropertyValue(String propertyName) {
        this.propertyValueList.remove(getPropertyValue(propertyName));
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        PropertyValue pv = getPropertyValue(propertyName);
        return pv != null ? pv.getValue() : null;
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
}
