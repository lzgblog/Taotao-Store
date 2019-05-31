package com.taotao.cart.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface CartService {

	//添加商品到购物车
	public TaotaoResult addItemCart(TbItem item, Integer num, Long userId);
	//获取购物车商品
	public List<TbItem> getCartList(Long userId);
	//更新数量
	public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num);
	//删除购物车商品
	public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId);
}
