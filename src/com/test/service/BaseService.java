package com.test.service;

import com.nannan.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService web_bbs;

    public BaseBaseService getBbs() {
        return web_bbs;
    }
    public void setBbs(BaseBaseService bbbsss) {
        this.web_bbs = bbbsss;
    }
    public BaseService() {
    }
    public void sayHello() {
        System.out.print("Base Service says hello");
        web_bbs.sayHello();
    }

    public String getHello() {
        return "Base Service get Hello.";
    }
}
