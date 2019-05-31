package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexPage {

	//访问地址：http://localhost:8082/index.html
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	@RequestMapping("/")
	public String index01(){
		return "index";
	}
}
