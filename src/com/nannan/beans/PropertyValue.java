package com.nannan.beans;

import lombok.Data;

@Data
public class PropertyValue {
    private String type;
    private String name;
    private Object value;
    private final boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public PropertyValue(String name, Object value) {
        this("", name, value, false);
    }

}
