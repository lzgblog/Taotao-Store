package com.taotao.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private HttpSolrServer solrServer;
	@Autowired
	private SearchItemMapper searchItemMapper;
	// 查询添加到索引库的数据
	@Override
	public TaotaoResult findIndexItem() {
		// 从数据库中获取检索的数据 
		List<SearchItem> items = searchItemMapper.searchItem();
		for (SearchItem item : items) {
			// 创建SolrInputDocument 将需要的数据放到索引库域中
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", item.getId().toString());
			document.addField("item_title", item.getTitle());
			document.addField("item_sell_point", item.getSell_point());
			document.addField("item_price", item.getPrice());
			document.addField("item_image", item.getImage());
			document.addField("item_category_name", item.getCategory_name());
			document.addField("item_desc", item.getItem_desc());
			// 添加到索引库
			try {
				solrServer.add(document);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 提交
		try {
			solrServer.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaotaoResult.ok();// 返回一个200的状态码 给调用的ajax
	}

	// 根据条件 从检索库中查询数据
	@Override
	public SearchResult findDataFromSearch(String queryString, Integer page) throws Exception {
		// 1.创建solrquery对象
		SolrQuery query = new SolrQuery();
		// 2.设置主查询条件
		if (StringUtils.isNotEmpty(queryString)) {
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");// 如果为空 查询所有数据
		}
		// 2.1设置过滤条件 
		// 设置分页
		if (page == null) {
			page = 1;
		}
		query.setStart((page - 1) * 20);// 从第几个开始
		query.setRows(20);// 一页显示多少个
		// 2.2.设置默认的搜索域
		query.set("df", "item_keywords");
		// 2.3设置高亮
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_title");//设置高亮显示的域
		// 3.根据HttpSolrServer获取检索的数据
		QueryResponse result = solrServer.query(query);
		SolrDocumentList documentList = result.getResults();
		//获取总记录数
		long count = documentList.getNumFound();
		//获取高光
		Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();
		//4.将检索的数据  封装到SearchResult类中itemList中
		List<SearchItem> itemList=new ArrayList<>();
		for (SolrDocument solrDocument : documentList) {
			//将solrdocument中的属性 一个个的设置到 searchItem中
			SearchItem item=new SearchItem();
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			item.setId(Long.parseLong(solrDocument.get("id").toString()));
			item.setImage(solrDocument.get("item_image").toString());
			//item.setItem_desc(item_desc);
			item.setPrice((Long)solrDocument.get("item_price"));
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			//取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			//判断list是否为空
			if(list!=null && list.size()>0){
				//有高亮
				item.setTitle(list.get(0));
			}else{
				item.setTitle(solrDocument.get("item_title").toString());
			}
			//将item 放入list集合中
			itemList.add(item);
		}
		SearchResult searchResult = new SearchResult();
		//将Item数据放到SearchResult
		searchResult.setItemList(itemList);
		//将总记录数放到SearchResult
		searchResult.setRecordCount(count);
		//5.设置SearchResult 的总页数
		long PageCount=(long)Math.ceil(count*1.0/20);
		searchResult.setPageCount(PageCount);
		return searchResult;
	}

}
