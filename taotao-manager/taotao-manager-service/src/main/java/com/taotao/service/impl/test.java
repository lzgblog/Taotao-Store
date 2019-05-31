package com.taotao.service.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TestMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class test {

	@Test
	public void test(){
		String path="classpath:spring/applicationContext-dao.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
		TestMapper mapper = context.getBean(TestMapper.class);
		PageHelper.startPage(1, 20);
		List<TbItem> list = mapper.findAllItem();
		PageInfo<TbItem> info = new PageInfo<>(list);
		List<TbItem> list2 = info.getList();
		for (TbItem tbItem : list2) {
			System.out.println(tbItem);
		}
		System.out.println(info.getTotal());
	}
}
