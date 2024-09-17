package com.test;

import com.nannan.web.bind.support.WebBindingInitializer;
import com.nannan.web.bind.WebDataBinder;

import java.util.Date;

public class DateInitializer implements WebBindingInitializer {
    @Override
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }
}
