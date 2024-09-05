package com.nannan.test;

import com.nannan.spring.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService bbbsss;

    public BaseBaseService getBbs() {
        return bbbsss;
    }
    public void setBbs(BaseBaseService bbbsss) {
        this.bbbsss = bbbsss;
    }
    public BaseService() {
    }
    public void sayHello() {
        System.out.print("Base Service says hello");
        bbbsss.sayHello();
    }
}
