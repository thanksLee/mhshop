package com.cafe24.mhshop.controller.api;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.cafe24.mhshop.config.TestAppConfig;
import com.cafe24.mhshop.config.TestWebConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, TestWebConfig.class})
@WebAppConfiguration
public class AdminCategoryControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		// DB category, item 테이블 초기화
		// DB 테스트용 데이터 insert

		// category insert
		// insert into category(no, name) values(1, 'test_category1')
		// insert into category(no, name) values(2, 'test_category2')
		
		
		// item insert
		// insert into item(no, name, description, money, thmbnail, display, category_no) values(1, 'test_item1', 'test_description1', 10000, 'test_thumbnail1', 'FALSE', 1)
		// insert into item(no, name, description, money, thmbnail, display, category_no) values(2, 'test_item2', 'test_description2', 20000, 'test_thumbnail2', 'FALSE', 2)
	}
	
	// 관리자 카테고리 등록
	@Test
	public void testA카테고리작성() throws Exception {
		ResultActions resultActions;
		
		// 카테고리명 Valid
		resultActions = mockMvc.perform(post("/api/admin/category/write")
				.param("name", "")
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());

		
		// 작성성공
		resultActions = mockMvc.perform(post("/api/admin/category/write")
				.param("name", "category_name3")
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	

	// 카테고리 리스트
	@Test
	public void testC카테고리리스트() throws Exception {
		
		ResultActions resultActions = mockMvc.perform(get("/api/admin/category/list").contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 성공햇는지
		// 포워드할 페이지를 리턴하는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("success")))
		.andExpect(jsonPath("$.data.forward", is("admin/category_list")));
		
	}
	
	// 관리자 카테고리 수정
	@Test
	public void testD카테고리수정() throws Exception {
		ResultActions resultActions;
		
		
		resultActions = mockMvc.perform(put("/api/admin/category/{no}/{name}", 2L, "test")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk());

	}
	
	// 관리자 카테고리 삭제
	@Test
	public void testE카테고리삭제() throws Exception {
		ResultActions resultActions;
		
		
		resultActions = mockMvc.perform(delete("/api/admin/category/{no}", 2L)
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk());

	}
	
	
	
	
}
