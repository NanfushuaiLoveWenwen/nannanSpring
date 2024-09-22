package com.test.service.aop;

public class ActionImpl3 implements Action{
    @Override
    public void doAction() {
        System.out.println("do action impl three");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something by action impl three");
    }
}
