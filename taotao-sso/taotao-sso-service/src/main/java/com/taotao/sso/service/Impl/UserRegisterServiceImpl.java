package com.taotao.sso.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserRegisterService;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;

	// 效验注册数据
	public TaotaoResult checkUserRegister(String param, Integer type) {

		// 1.注入mapper
		// 2.根据参数动态的生成查询的条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {// username
			if (StringUtils.isEmpty(param)) {
				return TaotaoResult.ok(false);
			}
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			// phone
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			// email
			criteria.andEmailEqualTo(param);
		} else {
			// 是非法的参数
			// return 非法的
			return TaotaoResult.build(400, "非法的参数");
		}
		// 3.调用mapper的查询方法 获取数据
		List<TbUser> tb = tbUserMapper.selectByExample(example);
		// 4.判断tb是否为空 不为空则说明已经被注册 返回false
		if (tb != null && tb.size() > 0) {
			return TaotaoResult.ok(false);
		}
		// 5.如果没查到数据 ---数据是可以用 true
		return TaotaoResult.ok(true);
	}

	// 注册用户信息
	@Override
	public TaotaoResult UserRegister(TbUser user) {
		// 1.注入mapper
		// 2.校验数据
		// 2.1 校验用户名和密码是否为空
		// 为空则返回错误提示
		if (StringUtils.isEmpty(user.getUsername())) {
			return TaotaoResult.build(400, "用户不能为空！");
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空！");
		}
		// 3.校验用户名是否被注册了
		// 被注册则返回错误提示
		TaotaoResult result1 = checkUserRegister(user.getUsername(), 1);
		// 当result1中的data属性是false 返回错误提示
		if (!(boolean) result1.getData()) {
			return TaotaoResult.build(400, "用户已经被注册！");
		}
		// 判断电话号码是否被注册
		if (StringUtils.isNotEmpty(user.getPhone())) {
			TaotaoResult result2 = checkUserRegister(user.getPhone(), 2);
			if (!(boolean) result2.getData()) {
				return TaotaoResult.build(400, "手机号已经被注册！");
			}
		}
		// 校验email是否被注册了
		if (StringUtils.isNotBlank(user.getEmail())) {
			TaotaoResult result3 = checkUserRegister(user.getEmail(), 3);
			if (!(boolean) result3.getData()) {
				// 数据不可用
				return TaotaoResult.build(400, "邮箱已经被注册！");
			}
		}
		//以上都没问题后执行
		//添加数据库表中的字段
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//对密码进行MD5加密
		String md5password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5password);
		//插入数据
		tbUserMapper.insertSelective(user);
		//返回taotaoresult
		return TaotaoResult.ok();
	}

}
