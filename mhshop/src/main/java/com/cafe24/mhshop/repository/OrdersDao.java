package com.cafe24.mhshop.repository;

import java.util.List;
import java.util.Map;

import com.cafe24.mhshop.vo.OrdersVo;

public interface OrdersDao {

	List<OrdersVo> selectList();						// 주문리스트
	OrdersVo selectOne(OrdersVo ordersVo);				// 주문상세
	Integer updateStatus(Map<String, String> map);		// 상태 변경
	Integer updateTrackingNum(Map<String, String> map);	// 운송장번호 변경

}