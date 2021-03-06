package com.cafe24.mhmall.frontend.controller;

import java.net.URI;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cafe24.mhmall.frontend.dto.ResponseJSONResult;
import com.cafe24.mhmall.frontend.security.AuthUser;
import com.cafe24.mhmall.frontend.service.CategoryService;
import com.cafe24.mhmall.frontend.service.ItemImgService;
import com.cafe24.mhmall.frontend.service.ItemService;
import com.cafe24.mhmall.frontend.util.MhmallRestTemplate;
import com.cafe24.mhmall.frontend.vo.MemberVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ItemService itemService;
	

	@RequestMapping({"", "/"})
	public String main(
			Model model
			) {
		
		// 카테고리 리스트 요청
		ResponseJSONResult<CategoryService.ListCategoryVo> rJsonCategory = categoryService.getList();
		model.addAttribute("categoryList", rJsonCategory.getData());
		
		
		// 최근상품리스트 (4개)
		ResponseJSONResult<ItemService.ListItemVo> rJsonItemList = itemService.getNewList(-1L, 4);
		model.addAttribute("itemList", rJsonItemList.getData());
		
		
		// 최근상품이미지 (3개)
		ResponseJSONResult<ItemService.ListMainImgVo> rJsonMainImgList = itemService.getNewImgList(3);
		model.addAttribute("mainImgList", rJsonMainImgList.getData());
		
				
		return "main/index";
	}
	
}
