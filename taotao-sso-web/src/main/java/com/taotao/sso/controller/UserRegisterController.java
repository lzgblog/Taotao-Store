package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;

@Controller
public class UserRegisterController {

	@Autowired
	private UserRegisterService userRegisterService;
	/**
	 * 效验注册数据
	 * url：/user/check/{param}/{type}
	 * 
	 * @param param
	 * @param type  1  2 3 
	 * @return
	 */
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult userRegisterCheck(@PathVariable String param,@PathVariable Integer type){
		//引入dubbo服务
		//注入
		//调用方法
		return userRegisterService.checkUserRegister(param, type);
	}
	/* 注册用户
	 * url:/user/register
	 *  参数：
	 *  username //用户名
		password //密码
		phone //手机号
		email //邮箱
	请求的方法：post
	返回值：json
	 */
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userRegister(TbUser user){
		return userRegisterService.UserRegister(user);
	}
}
