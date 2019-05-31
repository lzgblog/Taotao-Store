package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品相关的处理的service
 * @title ItemService.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface ItemService {
	
	/**
	 *  根据当前的页码和每页 的行数进行分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
	
	/**
	 * 添加商品
	 * @param tbItem
	 * @return
	 */
	public TaotaoResult addItem(TbItem tbItem);
	
	/**
	 * 修改商品信息
	 * @param tbItem
	 * @return
	 */
	public TaotaoResult updateItem(TbItem tbItem);
	
	/**
	 * 删除商品
	 * @param ids
	 * @return
	 */
	public TaotaoResult deleteItem(Long ids);
	/**
	 * 根据商品id查找商品信息   并在商品详情页面显示
	 * @param itemId
	 * @return
	 */
	public TbItem findItemDetailById(Long itemId);
	/**
	 * 根据商品id查找商品详情信息   并在商品详情页面显示
	 * @param itemId
	 * @return
	 */
	public TbItemDesc findItemDescById(Long itemId);
}
