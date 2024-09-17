package com.nannan.beans;

//converter between string and obj
public interface PropertyEditor {
    void setAsText(String text);

    void setValue(Object value);

    Object getValue();

    Object getAsText();
}
