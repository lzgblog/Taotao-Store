package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.TbItem;

/**
 * 测试接口 查询当前的时间
 * @title TestMapper.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface TestMapper {
	public String queryNow();
	public List<TbItem> findAllItem();
}
