package com.nannan.spring.context;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
    void addApplicationListener(ApplicationListener listener);
}
