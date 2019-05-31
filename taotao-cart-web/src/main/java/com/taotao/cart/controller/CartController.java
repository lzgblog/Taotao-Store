package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;//购物车业务
	@Autowired
	private ItemService itemService;//商品业务
	@Autowired
	private UserLoginService userLoginService;//用户登入业务
	//添加商品到购物车
	@RequestMapping(value="/cart/add/{itemId}",method=RequestMethod.GET)
	public String addItemCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 1.引入服务
		// 2.注入服务

		// 3.判断用户是否登录
			// 从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据token   查询到redis中存有的用户数据
		TaotaoResult result = userLoginService.getUserByToken(token);
		//根据商品id   查询商品数据
		TbItem tbItem = itemService.findItemDetailById(itemId);
		if(result.getStatus()==200){
			//status值200表示用户数据存在  即用户已经登录   获取用户数据
			TbUser user = (TbUser) result.getData();
			//调用添加到购物车方法
			if(num == null || "".equals(num)){
				num=1;
			}
			cartService.addItemCart(tbItem, num, user.getId());
		}else{
			//如果用户没有登入  让用户登入再进行添加购物车操作    
			//获取当前请求的url路径   并传到8088端口登入服务     再将该url设置给login页面获取    login页面的js判断是否有参  判断即可跳转
			//之后登录成功后会直接访问该方法   即可直接加入到购物车中
			//return "redirect:http://localhost:8088/page/login?redirect="+request.getRequestURL();
			return "redirect:http://sso.taotao.com/page/login?redirect="+request.getRequestURL();
		}
		return "cartSuccess";
	}
	// 展示购物车的列表
		@RequestMapping("/cart/cart")
		public String getCartList(HttpServletRequest request) {
			// 1.引入服务
			// 2.注入服务

			// 3.判断用户是否登录
			// 从cookie中获取用户的token信息
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			// 调用SSO的服务查询用户的信息
			TaotaoResult result = userLoginService.getUserByToken(token);

			System.out.println(result.getData());
			// 获取商品的数据
			if (result.getStatus() == 200) {
				// 4.如果已登录，调用service的方法
				TbUser user = (TbUser) result.getData();
				List<TbItem> cartList = cartService.getCartList(user.getId());
				System.out.println(cartList.size());
				request.setAttribute("cartList", cartList);
			} else {
				//如果用户没有登入  让用户登入再进行添加购物车操作
				return "redirect:http://sso.taotao.com/page/login?redirect="+request.getRequestURL();
			}
			return "cart";
		}
		
		// 删除购物车中的商品
		@RequestMapping("/cart/delete/{itemId}")
		public String deleteItemCartByItemId(@PathVariable Long itemId, HttpServletRequest request,
				HttpServletResponse response) {
			// 1.引入服务
			// 2.注入服务

			// 3.判断用户是否登录
			// 从cookie中获取用户的token信息
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			// 调用SSO的服务查询用户的信息
			TaotaoResult result = userLoginService.getUserByToken(token);

			// 获取商品的数据
			if (result.getStatus() == 200) {
				// 4.如果已登录，调用service的方法
				TbUser user = (TbUser) result.getData();
				// 删除
				cartService.deleteItemCartByItemId(user.getId(), itemId);
			} else {
				//如果用户没有登入  让用户登入再进行添加购物车操作
				return "redirect:http://sso.taotao.com/page/login?redirect="+request.getRequestURL();
			}
			return "redirect:/cart/cart";// 重定向
		}
		
		/**
		 * url:/cart/update/num/{itemId}/{num} 参数：商品的id 和更新后的数量 还有用户的id 返回值：json
		 */
		@RequestMapping("/cart/update/num/{itemId}/{num}")
		@ResponseBody
		public TaotaoResult updateItemCartByItemId(@PathVariable Long itemId, @PathVariable Integer num,
				HttpServletRequest request, HttpServletResponse response) {
			// 1.引入服务
			// 2.注入服务

			// 3.判断用户是否登录
			// 从cookie中获取用户的token信息
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			// 调用SSO的服务查询用户的信息
			TaotaoResult result = userLoginService.getUserByToken(token);

			// 获取商品的数据
			if (result.getStatus() == 200) {
				// 4.如果已登录，调用service的方法
				TbUser user = (TbUser) result.getData();
				// 更新商品的数量
				cartService.updateItemCartByItemId(user.getId(), itemId, num);
			} else {
				//如果用户没有登入  让用户登入再进行添加购物车操作
				return TaotaoResult.build(400, "用户没有登录！");
			}
			return TaotaoResult.ok();
		}
}
