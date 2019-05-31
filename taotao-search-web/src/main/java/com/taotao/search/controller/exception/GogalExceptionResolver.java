package com.taotao.search.controller.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//全局异常处理器   当controller页面出现异常时  该类会执行 
//需要实现HandlerExceptionResolver接口并在springmvc中创建该实例
public class GogalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception e) {
		//获取异常信息
		System.out.println(e.getMessage());
		e.getStackTrace();
		//通知
		System.out.println("发短信请求处理！！");
		//创建ModelAndView逻辑视图
		ModelAndView modelAndView = new ModelAndView();
		//显示错误的页面
		modelAndView.setViewName("error/exception");//springmvc中已经配置了视图解析器
		//返回给界面的错误数据
		modelAndView.addObject("message","网络繁忙！！请重试");
		return modelAndView;
	}

}
