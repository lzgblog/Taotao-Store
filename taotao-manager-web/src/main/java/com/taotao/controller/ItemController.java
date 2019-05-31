package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemservice;
	//url:/item/list
	//method:get
	//参数：page,rows
	//返回值:json
	//查询所有的商品信息
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//1.引入服务
		//2.注入服务
		//3.调用服务的方法
		return itemservice.getItemList(page, rows);
	}
	//添加商品
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem tbItem){
		
		return itemservice.addItem(tbItem);
	}
	//修改商品信息
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateItem(TbItem tbItem){
		
		return itemservice.updateItem(tbItem);
	}
	//删除商品
	@RequestMapping(value="/rest/item/delete")
	@ResponseBody
	public TaotaoResult deleteItem(Long ids){
		
		return itemservice.deleteItem(ids);
	}
}
