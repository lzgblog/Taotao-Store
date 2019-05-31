package com.taotao.search.service;


import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {
	//查询需要添加到索引库的数据
	public TaotaoResult findIndexItem();
	//获取检索库中的数据
	public SearchResult findDataFromSearch(String queryString, Integer page) throws Exception;
}
