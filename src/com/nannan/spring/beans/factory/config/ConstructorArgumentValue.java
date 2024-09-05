package com.nannan.spring.beans.factory.config;

import lombok.Data;

@Data
public class ConstructorArgumentValue {
    private Object value;
    private String type;
    private String name;

    public ConstructorArgumentValue(String type, String name, Object value) {
        this.value = value;
        this.type = type;
        this.name = name;
    }

    public ConstructorArgumentValue(Object value, String type) {
        this(type, null, value);
    }
}
