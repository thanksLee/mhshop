package com.cafe24.mhmall.service;

import java.util.List;

import com.cafe24.mhmall.vo.OptionDetailVo;

public interface OptionDetailService {

	List<OptionDetailVo> getListByItemNoAndLevel(Long itemNo, Long level);	// 상품번호에 속하고 레벨에 따른 상세옵션 리스트
	boolean add(OptionDetailVo optionDetailVo);								// 상세옵션 추가
	boolean delete(Long no);												// 상세옵션 삭제
	boolean hasOptionDetail(Long optionDetailNo1, Long optionDetailNo2);	// 존재하는 상세옵션인지 확인

}
