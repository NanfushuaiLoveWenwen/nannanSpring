package com.test.service.aop;

public class ActionImpl2 implements Action{
    @Override
    public void doAction() {
        System.out.println("do action impl two");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something by action impl two");
    }
}
