package com.test;

import com.nannan.context.ApplicationEvent;
import com.nannan.context.ApplicationListener;
import com.nannan.context.ContextRefreshedEvent;

public class MyListener implements ApplicationListener<ContextRefreshedEvent> {
	   @Override
	   public void onApplicationEvent(ContextRefreshedEvent event) {
	      System.out.println(".........refreshed.........beans count : " + event.getApplicationContext().getBeanDefinitionCount());
	   }
}

