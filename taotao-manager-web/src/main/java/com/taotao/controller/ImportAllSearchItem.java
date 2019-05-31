package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;

@Controller
public class ImportAllSearchItem {

	@Autowired
	private SearchService searchService;
	@RequestMapping("/index/importAll")
	@ResponseBody
	public TaotaoResult importAllItem(){
		return searchService.findIndexItem();
	}
}
