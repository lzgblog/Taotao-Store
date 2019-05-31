package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper mapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;// 商品详情信息
	@Autowired
	private JedisClient jedisClient;// 注入jedisclient

	// 查询所有商品数据 并进行分页
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		// 1.设置分页的信息 使用pagehelper
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 30;
		PageHelper.startPage(page, rows);
		// 2.注入mapper
		// 3.创建example 对象 不需要设置查询条件
		TbItemExample example = new TbItemExample();
		// 4.根据mapper调用查询所有数据的方法
		List<TbItem> list = mapper.selectByExample(example);
		// 5.获取分页的信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		// 6.封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		// 7.返回
		return result;
	}
	
	//添加商品
	@Override
	public TaotaoResult addItem(TbItem tbItem) {
		tbItem.setStatus((byte) 1);
		tbItem.setCid(560l);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(tbItem.getCreated());
		mapper.insert(tbItem);
		return TaotaoResult.ok();
	}
	
	//修改商品信息
	public TaotaoResult updateItem(TbItem tbItem){
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(tbItem.getCreated());
		mapper.updateByPrimaryKey(tbItem);
		return TaotaoResult.ok();
	}
	//删除商品
	public TaotaoResult deleteItem(Long ids){
		mapper.deleteByPrimaryKey(ids);
		return TaotaoResult.ok();
	}

	// 根据商品id查找商品信息 并在商品详情页面显示
	public TbItem findItemDetailById(Long itemId) {

		// 给高并发量的数据添加缓存 并设置该缓存的存活时间
		// 当redis缓存中有数据时
		String str = jedisClient.get("ITEM_INFO:" + itemId + ":BASE");
		// 如果缓存中的不为空时
		if (StringUtils.isNotEmpty(str)) {
			// 设置缓存时间
			System.out.println("有缓存了！！！");
			jedisClient.expire("ITEM_INFO:" + itemId + ":BASE", 60*60*24);
			return JsonUtils.jsonToPojo(str, TbItem.class);// 将redis中json格式的数据转成TbItem格式
		}

		// 当redis缓存中没有数据时   向数据库中查询数据
		TbItem item = mapper.selectByPrimaryKey(itemId);
		try {
			// 当第一次调用该方法时 将在数据查询出来并放入到redis缓存中
			// 1.注入JedisClient 调用该类中的添加set方法 添加缓存 String类型存储
			// ITEM_INFO:123:BASE（key），以json格式的数据（ value）
			jedisClient.set("ITEM_INFO:" + itemId + ":BASE",
					JsonUtils.objectToJson(item));
			// 2.设置缓存存活时间
			jedisClient.expire("ITEM_INFO:" + itemId + ":BASE", 60*60*24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	//******注意添加redis缓存时   上下两个方法中的key不能一样
	// 根据商品id查找商品详情信息 并在商品详情页面显示
	public TbItemDesc findItemDescById(Long itemId) {

		// 给高并发量的数据添加缓存 并设置该缓存的存活时间
		// 当redis缓存中有数据时
		String str = jedisClient.get("ITEM_INFO_KEY:" + itemId + ":BASE");
		// 如果缓存中的不为空时
		if (StringUtils.isNotEmpty(str)) {
			// 设置缓存时间
			jedisClient.expire("ITEM_INFO_KEY:" + itemId + ":BASE", 60*60*24);
			return JsonUtils.jsonToPojo(str, TbItemDesc.class);// 将redis中json格式的数据转成TbItem格式
		}

		// 当redis缓存中没有数据时  向数据库中查询数据
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			// 当第一次调用该方法时 将在数据查询出来并放入到redis缓存中
			// 1.注入JedisClient 调用该类中的添加set方法 添加缓存 String类型存储
			// ITEM_INFO:123:BASE（key），以json格式的数据（ value）
			jedisClient.set("ITEM_INFO_KEY:" + itemId + ":BASE",
					JsonUtils.objectToJson(itemDesc));
			// 2.设置缓存存活时间
			jedisClient.expire("ITEM_INFO_KEY:" + itemId + ":BASE", 60*60*24);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemDesc;
	}

}
