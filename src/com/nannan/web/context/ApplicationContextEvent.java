package com.nannan.web.context;

import com.nannan.context.ApplicationContext;
import com.nannan.context.ApplicationEvent;

public abstract class ApplicationContextEvent extends ApplicationEvent {
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
