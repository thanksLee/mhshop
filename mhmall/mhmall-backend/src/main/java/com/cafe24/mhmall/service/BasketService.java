package com.cafe24.mhmall.service;

import java.util.List;

import com.cafe24.mhmall.vo.BasketVo;

public interface BasketService {

	boolean guestDeleteByCnt(String guestSession);				// 장바구니 리스트 중에 수량보다 재고가 없는 것 일괄삭제
	boolean guestNewTime(String guestSession);					// 입력 시간을 현재로 갱신(비회원의 장바구니는 30개월간만 유지된다)
	List<BasketVo> getListByGuest(String guestSession);			// 장바구니 리스트

}