package com.test;


import com.nannan.beans.factory.annotation.Autowired;
import com.test.service.BaseService;
import com.nannan.web.bind.annotation.RequestMapping;

public class

HelloWorldBean {
	@Autowired
	BaseService web_baseservice;
	
	@RequestMapping("/test1")
	public String doTest1() {
		return "test 1, hello world!";
	}
	@RequestMapping("/test2")
	public String doTest2() {
		return "test 2, hello world!";
	}
	@RequestMapping("/test3")
	public String doTest3() {
		return web_baseservice.getHello();
	}}
