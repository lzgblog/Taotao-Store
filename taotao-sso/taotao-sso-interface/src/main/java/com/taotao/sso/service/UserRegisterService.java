package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserRegisterService {
	/**
	 * 效验注册数据
	 * @param param 注册参数
	 * @param type 参数类型 ： 1.用户名  2.手机号 3.邮箱
	 * @return
	 */
	public TaotaoResult checkUserRegister(String param, Integer type);
	/**
	 * 注册用户
	 * @param user  注册用户信息
	 * @return
	 */
	public TaotaoResult UserRegister(TbUser user);
}
