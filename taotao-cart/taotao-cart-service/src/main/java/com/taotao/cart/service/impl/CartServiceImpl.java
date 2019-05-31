package com.taotao.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cart.jedis.JedisClient;
import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	//添加到购物车
	@Override
	public TaotaoResult addItemCart(TbItem item, Integer num, Long userId) {
		//从redis缓存中查找商品是否存在   
		String hget = jedisClient.hget("CART__KEY:"+userId+"", item.getId()+"");
		TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
		//如果redis中存在数据
		if(tbItem!=null){
			//如果查询到数据  则将数量添加  原有+新添加
			tbItem.setNum(tbItem.getNum()+num);
			//将新添加的数量添加到redis中
			jedisClient.hset("CART__KEY:"+userId+"", item.getId()+"", JsonUtils.objectToJson(tbItem));
		}else{
			//如果不存在  将数量添加到TbItem中
			item.setNum(num);
			//添加到redis中
			jedisClient.hset("CART__KEY:"+userId+"", item.getId()+"", JsonUtils.objectToJson(item));
		}
		
		return TaotaoResult.ok();
	}
	
	// 获取购物车的商品的列表
		public List<TbItem> getCartList(Long userId) {
			Map<String, String> map = jedisClient.hgetAll("CART__KEY:"+userId + "");
			//
			List<TbItem> list = new ArrayList<>();
			if (map != null) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String value = entry.getValue();// 商品的jSON数据
					// 转成POJO
					TbItem item = JsonUtils.jsonToPojo(value, TbItem.class);
					list.add(item);
				}
			}
			return list;
		}
		//更新数量
		@Override
		public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num) {
			//1.根据用户id和商品的id获取商品的对象
			//从redis缓存中查找商品是否存在   
			String hget = jedisClient.hget("CART__KEY:"+userId+"", itemId+"");
			TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
			//判断是否存在
			if(tbItem!=null){
				//2.更新数量
				tbItem.setNum(num);
				//设置回redis中
				jedisClient.hset("CART__KEY:"+userId+"", itemId+"", JsonUtils.objectToJson(tbItem));
			}else{
				//不管啦
			}
			return TaotaoResult.ok();
		}

		//删除购物车商品   即将redis中缓存的数据删除
		@Override
		public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId) {
			jedisClient.hdel("CART__KEY:"+userId+"", itemId+"");
			return TaotaoResult.ok();
		}
}
