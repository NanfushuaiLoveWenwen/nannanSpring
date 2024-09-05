package com.nannan.test;

import lombok.Data;

@Data
public class AServiceImpl implements AService{
    private String name;
    private int level;
    private String property1;
    private String property2;
    private BaseService ref1;

    public AServiceImpl(){};

    public AServiceImpl(String name, int level){
        this.name = name;
        this.level = level;
        System.out.println(this.name + ", " + this.level);
    }

    @Override
    public void sayHello() {
        System.out.println(this.property1 + ", " + this.property2);
    }
}
