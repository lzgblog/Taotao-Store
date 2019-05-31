package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;


public interface ContentCategoryService{


	//根据parentid查询分类节点
	public List<EasyUITreeNode> getContentCategoryList(Long parentId); 
	//创建内容分类
	public TaotaoResult createContentCategory(Long parentId, String name);
}
