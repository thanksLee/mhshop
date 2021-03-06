package com.cafe24.mhmall.controller.api;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.annotations.ApiImplicitParam;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = true)
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String myAuthorization;

	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		

		ResultActions resultActions;
		
		// 사용자 로그인
		resultActions = mockMvc.perform(post("/api/member/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"id\":\"test_id2\","
						+ "\"password\":\"test\""
						+ "}"));
		// 응답이 200 인지
		MvcResult mvcResult = resultActions.andDo(print())
		.andExpect(status().isOk())
		.andReturn();

		// 로그인키 가져오기
		String content = mvcResult.getResponse().getContentAsString();
		JsonParser Parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) Parser.parse(content);
		myAuthorization = jsonObj.get("data").getAsJsonObject().get("mockToken").getAsString();
	}
	

	// 관리자 상품 리스트
	@Test
	public void testA관리자상품리스트() throws Exception {
		ResultActions resultActions;
		
		
		resultActions = mockMvc.perform(get("/api/admin/item/list/{cateogryNo}", -1L)
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		resultActions.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data[1].no", is(1)))
		.andExpect(jsonPath("$.data[1].name", is("test_item1")))
		.andExpect(jsonPath("$.data[1].description", is("test_description1")))
		.andExpect(jsonPath("$.data[1].money", is(10000)))
		.andExpect(jsonPath("$.data[1].thumbnail", is("test_thumbnail1")))
		.andExpect(jsonPath("$.data[1].display", is("TRUE")))
		.andExpect(jsonPath("$.data[1].categoryNo", is(1)))

		.andExpect(jsonPath("$.data[0].no", is(2)))
		.andExpect(jsonPath("$.data[0].name", is("test_item2")))
		.andExpect(jsonPath("$.data[0].description", is("test_description2")))
		.andExpect(jsonPath("$.data[0].money", is(20000)))
		.andExpect(jsonPath("$.data[0].thumbnail", is("test_thumbnail2")))
		.andExpect(jsonPath("$.data[0].display", is("FALSE")))
		.andExpect(jsonPath("$.data[0].categoryNo", is(1)));
		
	}
	
	
	
	// 관리자 상품 등록
	@Test
	public void testB상품등록() throws Exception {
		ResultActions resultActions;
		
		// 상품이름 Valid
		resultActions = mockMvc.perform(post("/api/admin/item")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"name\":\"\","
						+ "\"description\":\"test_description2\","
						+ "\"money\":\"30000\","
						+ "\"thumbnail\":\"test_thumbnail3\","
						+ "\"categoryNo\":\"1\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 상품금액 Valid
		resultActions = mockMvc.perform(post("/api/admin/item")
				.param("name", "test_name")
				.param("description", "test_description2")
				.param("money", "")
				.param("thumbnail", "test_thumbnail3")
				.param("categoryNo", "1")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"name\":\"test_name\","
						+ "\"description\":\"test_description2\","
						+ "\"money\":\"\","
						+ "\"thumbnail\":\"test_thumbnail3\","
						+ "\"categoryNo\":\"1\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 성공했을 때
		resultActions = mockMvc.perform(post("/api/admin/item")
				.param("name", "test_name")
				.param("description", "test_description2")
				.param("money", "1000")
				.param("thumbnail", "test_thumbnail3")
				.param("categoryNo", "1")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"name\":\"test_name\","
						+ "\"description\":\"test_description2\","
						+ "\"money\":\"1000\","
						+ "\"thumbnail\":\"test_thumbnail3\","
						+ "\"categoryNo\":\"1\""
						+ "}"));
		
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	
	

	// 관리자 상품 삭제
	@Test
	public void testC상품삭제() throws Exception {
		ResultActions resultActions;
		
		// 상품삭제
		resultActions = mockMvc.perform(delete("/api/admin/item")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\""
						+ "}"));
		// 응답이 200 인지
		resultActions.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
		
	}
	


	// 관리자 상세옵션리스트
	@Test
	public void testD상세옵션리스트() throws Exception {
		ResultActions resultActions;
		
		// 옵션레벨 Valid
		resultActions = mockMvc.perform(get("/api/admin/item/optiondetail/{itemNo}/{level}", 1L, 3L)
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 성공
		resultActions = mockMvc.perform(get("/api/admin/item/optiondetail/{itemNo}/{level}", 1L, 1L)
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data[0].no", is(1)))
		.andExpect(jsonPath("$.data[0].optionName", is("파란색")))
		.andExpect(jsonPath("$.data[0].level", is(1)))
		.andExpect(jsonPath("$.data[0].itemNo", is(1)));
		
	}
	
	
	
	
	
	

	// 관리자 상품수정
	@Test
	public void testE상품수정() throws Exception {
		ResultActions resultActions;

		// 상품명 Valid
		resultActions = mockMvc.perform(put("/api/admin/item")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\","
						+ "\"name\":\"\","
						+ "\"description\":\"test_description11\","
						+ "\"money\":\"11000\","
						+ "\"thumbnail\":\"test_thumbnail11\","
						+ "\"categoryNo\":\"2\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 상품금액 Valid
		resultActions = mockMvc.perform(put("/api/admin/item")
				.param("no", "1")
				.param("name", "change!!")
				.param("description", "test_description11")
				.param("money", "-1")
				.param("thumbnail", "test_thumbnail11")
				.param("categoryNo", "2")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\","
						+ "\"name\":\"change!!\","
						+ "\"description\":\"test_description11\","
						+ "\"money\":\"-1\","
						+ "\"thumbnail\":\"test_thumbnail11\","
						+ "\"categoryNo\":\"2\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 성공
		resultActions = mockMvc.perform(put("/api/admin/item")
				.param("no", "1")
				.param("name", "change!!")
				.param("description", "test_description11")
				.param("money", "11000")
				.param("thumbnail", "test_thumbnail11")
				.param("categoryNo", "2")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\","
						+ "\"name\":\"change!!\","
						+ "\"description\":\"test_description11\","
						+ "\"money\":\"11000\","
						+ "\"thumbnail\":\"test_thumbnail11\","
						+ "\"categoryNo\":\"2\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
		
	}
	


	

	// 관리자 상품진열여부정
	@Test
	public void testF상품진열여부수정() throws Exception {
		ResultActions resultActions;
		
		// 진열상태 Valid
		resultActions = mockMvc.perform(put("/api/admin/item/display")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"2\","
						+ "\"display\":\"ttttrue\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 성공
		resultActions = mockMvc.perform(put("/api/admin/item/display")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\","
						+ "\"display\":\"TRUE\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	
	
	
	
	// 관리자 상품이미지저장
	@Test
	public void testG상품이미지저장() throws Exception {
		ResultActions resultActions;

		// 상품번호 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"\","
						+ "\"itemImg\":\"test_img\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 없는 상품번호 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"99\","
						+ "\"itemImg\":\"test_img\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 상품이미지 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"1\","
						+ "\"itemImg\":\"\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 성공
		resultActions = mockMvc.perform(post("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"1\","
						+ "\"itemImg\":\"test_img\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	

	// 관리자 상품이미지 삭제
	@Test
	public void testH상품이미지삭제() throws Exception {
		ResultActions resultActions;

		// 없는 이미지 삭제
		resultActions = mockMvc.perform(delete("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"99\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(false)));
		

		// 성공
		resultActions = mockMvc.perform(delete("/api/admin/item/img")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	
	
	
	
	// 관리자 상세옵션 저장
	@Test
	public void testI상세옵션저장() throws Exception {
		ResultActions resultActions;

		// 옵션이름 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"optionName\":\"\","
						+ "\"level\":\"1\","
						+ "\"itemNo\":\"1\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 옵션레벨 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"optionName\":\"option_name\","
						+ "\"level\":\"3\","
						+ "\"itemNo\":\"1\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 상품번호 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"optionName\":\"option_name\","
						+ "\"level\":\"3\","
						+ "\"itemNo\":\"\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 없는 상품번호 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"optionName\":\"option_name\","
						+ "\"level\":\"3\","
						+ "\"itemNo\":\"99\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 성공
		resultActions = mockMvc.perform(post("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"optionName\":\"option_name\","
						+ "\"level\":\"1\","
						+ "\"itemNo\":\"1\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	

	
	// 관리자 상세옵션 삭제
	@Test
	public void testJ상세옵션삭제() throws Exception {
		ResultActions resultActions;

		// 옵션에 이미 사용중인 상세옵션 실패
		resultActions = mockMvc.perform(delete("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());


		// 없는 상세옵션번호
		resultActions = mockMvc.perform(delete("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"99\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(false)));
		
		
		// 성공
		resultActions = mockMvc.perform(delete("/api/admin/item/optiondetail")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"4\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	
	
	
	

	// 관리자 옵션저장
	@Test
	public void testK옵션저장() throws Exception {
		ResultActions resultActions;

		// 수량 Valid
		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"1\","
						+ "\"optionDetailNo1\":\"1\","
						+ "\"optionDetailNo2\":\"4\","
						+ "\"cnt\":\"-5\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 존재하지 않는 상품번호 실패
		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"99\","
						+ "\"optionDetailNo1\":\"1\","
						+ "\"optionDetailNo2\":\"4\","
						+ "\"cnt\":\"5\""
						+ "}"));
		// 응답이400 인지
		resultActions
		.andExpect(status().isBadRequest());
		
		
		// 존재하지 않은 상세옵션번호 실패
		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"1\","
						+ "\"optionDetailNo1\":\"99\","
						+ "\"optionDetailNo2\":\"\","
						+ "\"cnt\":\"5\""
						+ "}"));
		// 응답이 400 인지
		resultActions
		.andExpect(status().isBadRequest());
		

		// 성공
		resultActions = mockMvc.perform(post("/api/admin/item/option")
				.param("itemNo", "1")
				.param("optionDetailNo1", "1")
				.param("optionDetailNo2", "4")
				.param("cnt", "5")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"itemNo\":\"1\","
						+ "\"optionDetailNo1\":\"1\","
						+ "\"optionDetailNo2\":\"4\","
						+ "\"cnt\":\"5\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
	
	

	// 관리자 옵션삭제
	@Test
	public void testL옵션삭제() throws Exception {
		ResultActions resultActions;

		// 없는 옵션 삭제
		resultActions = mockMvc.perform(delete("/api/admin/item/option")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"99\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(false)));
		

		// 성공
		resultActions = mockMvc.perform(delete("/api/admin/item/option")
				.header("MyAuthorization", "Basic " + myAuthorization)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"no\":\"1\""
						+ "}"));
		// 응답이 200 인지
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data", is(true)));
	}
	
}
