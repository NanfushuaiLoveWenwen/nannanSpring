package com.nannan.spring.context;

import java.util.ArrayList;
import java.util.List;

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher{
    List<ApplicationListener> listeners = new ArrayList<>();


    @Override
    public void publishEvent(ApplicationEvent event) {
        listeners.forEach(listener->listener.onApplicationEvent(event));
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        listeners.add(listener);
    }
}
