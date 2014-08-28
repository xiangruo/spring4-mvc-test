package com.sishuok.mvc.my;

import com.hhb.mvc.controller.UserController;
import com.sishuok.matcher.HasProperty;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "spring-mvc-test/src/main/webapp")
@ContextHierarchy({
		@ContextConfiguration(name = "parent", locations = "classpath:spring-config.xml"),
		@ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml") })
public class ServerControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void test1() throws Exception {

		// 测试普通控制器
		mockMvc.perform(get("/user/{id}", 1))
				// 执行请求
				.andExpect(model().attributeExists("user"))
				// 验证存储模型数据
				.andExpect(
						model().attribute("user",
								hasProperty("name", equalTo("zhang")))) // 验证存储模型数据
				.andExpect(view().name("user/view")) // 验证viewName
				.andExpect(forwardedUrl("/WEB-INF/jsp/user/view.jsp"))// 验证视图渲染时forward到的jsp
				.andExpect(status().isOk())// 验证状态码
				.andDo(print()); // 输出MvcResult到控制台
	}

	// 找不到控制器，404测试
	@Test
	public void test2() throws Exception {

		MvcResult result = mockMvc.perform(get("/user2/{id}", 1))
				.andDo(print()).andExpect(status().isNotFound()).andReturn();
		Assert.assertNull(result.getModelAndView()); // 自定义断言

	}

	@Test
	public void test3() throws Exception {
		// 得到MvcResult自己验证
		MvcResult result = mockMvc.perform(get("/user/{id}", 1))// 执行请求
				.andReturn(); // 返回MvcResult
		Assert.assertNotNull(result.getModelAndView().getModel().get("user")); // 自定义断言
	}
	@Test
	public void test4() throws Exception {
		//验证请求参数绑定
        mockMvc.perform(post("/user").param("name", "zhang")) //执行传递参数的POST请求(也可以post("/user?name=zhang"))
               .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("create")) //验证执行的控制器方法名
                .andExpect(model().hasNoErrors()) //验证页面没有错误
                .andExpect(flash().attributeExists("success")) //验证存在flash属性
                .andExpect(view().name("redirect:/user")); //验证视图
		
	}

}
