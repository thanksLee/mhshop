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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.cafe24.mhshop.config.AppConfig;
import com.cafe24.mhshop.config.TestWebConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.annotations.ApiImplicitParam;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestWebConfig.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Rollback(value = true)
@Transactional
public class AdminItemControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	

	// 관리자 상품 리스트
	@Test
	public void testA관리자상품리스트() throws Exception {
		ResultActions resultActions;
		
		// 관리자 로그인
		resultActions = mockMvc.perform(post("/api/member/login")
				.param("id", "test_id2")
				.param("password", "testpassword2!")
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		MvcResult mvcResult = resultActions
		.andExpect(status().isOk())
		.andReturn();

		// 로그인키 가져오기
		String content = mvcResult.getResponse().getContentAsString();
		JsonParser Parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) Parser.parse(content);
		String mockToken = jsonObj.get("data").getAsString();
		
		
		
		resultActions = mockMvc.perform(get("/api/admin/item/list")
				.param("mockToken", mockToken)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.itemList[1].no", is(1)))
		.andExpect(jsonPath("$.data.itemList[1].name", is("test_item1")))
		.andExpect(jsonPath("$.data.itemList[1].description", is("test_description1")))
		.andExpect(jsonPath("$.data.itemList[1].money", is(10000)))
		.andExpect(jsonPath("$.data.itemList[1].thumbnail", is("test_thumbnail1")))
		.andExpect(jsonPath("$.data.itemList[1].display", is("FALSE")))
		.andExpect(jsonPath("$.data.itemList[1].categoryNo", is(1)))

		.andExpect(jsonPath("$.data.itemList[0].no", is(2)))
		.andExpect(jsonPath("$.data.itemList[0].name", is("test_item2")))
		.andExpect(jsonPath("$.data.itemList[0].description", is("test_description2")))
		.andExpect(jsonPath("$.data.itemList[0].money", is(20000)))
		.andExpect(jsonPath("$.data.itemList[0].thumbnail", is("test_thumbnail2")))
		.andExpect(jsonPath("$.data.itemList[0].display", is("FALSE")))
		.andExpect(jsonPath("$.data.itemList[0].categoryNo", is(1)))
		
		.andExpect(jsonPath("$.data.categoryList[0].no", is(1)))
		.andExpect(jsonPath("$.data.categoryList[0].name", is("test_category1")))
		.andExpect(jsonPath("$.data.categoryList[1].no", is(2)))
		.andExpect(jsonPath("$.data.categoryList[1].name", is("test_category2")));
		
	}
	
	
	
	// 관리자 상품 등록
	@Test
	public void testB상품등록() throws Exception {
		ResultActions resultActions;
		
		// 관리자 로그인
		resultActions = mockMvc.perform(post("/api/member/login")
				.param("id", "test_id2")
				.param("password", "testpassword2!")
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		MvcResult mvcResult = resultActions
		.andExpect(status().isOk())
		.andReturn();

		// 로그인키 가져오기
		String content = mvcResult.getResponse().getContentAsString();
		JsonParser Parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) Parser.parse(content);
		String mockToken = jsonObj.get("data").getAsString();
		


		// 상품이름 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/write")
				.param("name", "")
				.param("description", "test_description2")
				.param("money", "30000")
				.param("thumbnail", "test_thumbnail3")
				.param("categoryNo", "1")
				.param("mockToken", mockToken)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 상품금액 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/write")
				.param("name", "test_name")
				.param("description", "test_description2")
				.param("money", "")
				.param("thumbnail", "test_thumbnail3")
				.param("categoryNo", "1")
				.param("mockToken", mockToken)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 성공했을 때
		resultActions = mockMvc.perform(post("/api/admin/item/write")
				.param("name", "test_name")
				.param("description", "test_description2")
				.param("money", "1000")
				.param("thumbnail", "test_thumbnail3")
				.param("categoryNo", "1")
				.param("mockToken", mockToken)
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	

	
	// 관리자 상품 DB에 수정 페이지
	@Test
	public void testD상품수정_페이지() throws Exception {
		
		ResultActions resultActions = mockMvc.perform(get("/api/admin/item/edit/{no}",1L)
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk());
		
	}
	
	// 관리자 상품 DB에 수정 NO Valid
	@Test
	public void testD상품수정_NO_Valid() throws Exception {
		
		ResultActions resultActions = mockMvc.perform(put("/api/admin/item/edit")
				.param("no", "")
				.param("name", "test_item11")
				.param("description", "test_description11")
				.param("money", "11000")
				.param("thumbnail", "test_thumbnail11")
				.param("categoryNo", "2")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패햇는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}
	
	// 관리자 상품 DB에 수정 이름 Valid
	@Test
	public void testD상품수정_이름_Valid() throws Exception {
		
		ResultActions resultActions = mockMvc.perform(put("/api/admin/item/edit")
				.param("no", "1")
				.param("name", "")
				.param("description", "test_description11")
				.param("money", "11000")
				.param("thumbnail", "test_thumbnail11")
				.param("categoryNo", "2")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패햇는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}

	// 관리자 상품 DB에 수정
	@Test
	public void testD상품수정() throws Exception {
		
		ResultActions resultActions = mockMvc.perform(put("/api/admin/item/edit")
				.param("no", "1")
				.param("name", "test_items")
				.param("description", "test_description11")
				.param("money", "11000")
				.param("thumbnail", "test_thumbnail11")
				.param("categoryNo", "2")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk());
		
	}
	


	

	// 관리자 상품 진열여부 DB에 수정 Valid
	@Test
	public void testE상품진열여부수정_진열여부_Valid() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(put("/api/admin/item/edit/display/{no}", 2L)
				.param("display", "")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패햇는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
		
	}

	// 관리자 상품 진열여부 DB에 수정
	@Test
	public void testE상품진열여부수정() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(put("/api/admin/item/edit/display/{no}", 2L)
				.param("display", "FALSE")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패햇는지
		resultActions
		.andExpect(status().isOk());
		
	}
	
	


	// 관리자 상품 이미지를 DB에 저장 상품번호 Valid
	@Test
	public void testF상품이미지작성_상품번호_Valid() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(post("/api/admin/item/itemimg")
				.param("itemNo", "")
				.param("itemImg", "test_img3")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	
	// 관리자 상품 이미지를 DB에 저장 상품이미지 Valid
	@Test
	public void testF상품이미지작성_상품이미지_Valid() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(post("/api/admin/item/itemimg")
				.param("itemNo", "1")
				.param("itemImg", "")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	
	

	// 관리자 상품 이미지를 DB에 저장
	@Test
	public void testF상품이미지작성() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(post("/api/admin/item/itemimg")
				.param("itemNo", "1")
				.param("itemImg", "test_img")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk());
	}
	
	
	// 관리자 상품 상세옵션 추가 레벨 Valid
	@Test
	public void testG상세옵션작성_레벨_Valid() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.param("optionName", "초록색")
				.param("level", "")
				.param("itemNo", "1")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	

	// 관리자 상품 상세옵션 추가
	@Test
	public void testG상세옵션작성() throws Exception {
		ResultActions resultActions;
		
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.param("optionName", "초록색")
				.param("level", "1")
				.param("itemNo", "1")
				.contentType(MediaType.APPLICATION_JSON));
		
		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk());
	}
	
	
	// 관리자 상품 옵션 추가 수량 Valid
	@Test
	public void testH옵션작성_수량_Valid() throws Exception {
		ResultActions resultActions;

		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.param("itemNo", "1")
				.param("optionDetail1", "1")
				.param("optionDetail2", "4")
				.param("cnt", "-5")
				.contentType(MediaType.APPLICATION_JSON));

		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is("fail")));
	}
	

	// 관리자 상품 옵션 추가
	@Test
	public void testH옵션작성() throws Exception {
		ResultActions resultActions;

		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.param("itemNo", "1")
				.param("optionDetail1", "1")
				.param("optionDetail2", "4")
				.param("cnt", "5")
				.contentType(MediaType.APPLICATION_JSON));

		// 응답이 200 인지
		// 결과가 실패했는지
		resultActions
		.andExpect(status().isOk());
	}
	
}
