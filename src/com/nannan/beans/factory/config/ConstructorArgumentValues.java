package com.nannan.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> argumentValueList = new ArrayList<>();

    public ConstructorArgumentValues(){};

    public void addArgumentValue(ConstructorArgumentValue argumentValue){
        argumentValueList.add(argumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index){
        return argumentValueList.get(index);
    }

    public int getArgumentCount(){
        return argumentValueList.size();
    }

    public boolean isEmpty(){
        return argumentValueList.isEmpty();
    }
}
