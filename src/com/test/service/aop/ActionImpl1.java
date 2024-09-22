package com.test.service.aop;

public class ActionImpl1 implements Action{
    @Override
    public void doAction() {
        System.out.println("do action impl one");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something by action impl one");
    }
}
