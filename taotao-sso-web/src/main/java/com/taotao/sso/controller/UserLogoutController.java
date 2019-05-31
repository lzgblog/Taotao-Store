package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.sso.service.UserLoginService;

@Controller
public class UserLogoutController {

	@Autowired
	private UserLoginService userLoginService;

	/**
	 * url:/user/logout/{token}
	 * 参数：token
	 * 请求限定的方法：get
	 */
	@RequestMapping(value="/user/logout/{token}")
	public String logout(@PathVariable String token){
		userLoginService.userLogout(token);
		return "redirect:http://www.taotao.com/index";
	}
}
