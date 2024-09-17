package com.nannan.context;

import com.nannan.web.context.ApplicationContextEvent;

public class ContextRefreshedEvent extends ApplicationContextEvent {
    private static final long serialVersionUID = 1L;

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

    public String toString() {
        return this.msg;
    }
}
