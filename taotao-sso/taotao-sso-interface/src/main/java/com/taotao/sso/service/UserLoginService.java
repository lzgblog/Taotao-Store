package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

public interface UserLoginService {

	/**
	 * 根据用户名和密码登入
	 * @param username
	 * @param password
	 * @return
	 */
	public TaotaoResult userLogin(String username,String password);
	/**
	 * 根据token 从redis缓存中  获取用户信息
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);
	/**
	 * 用户注销    将redis中的token缓存删除
	 * @param token
	 */
	public void userLogout(String token);
}
