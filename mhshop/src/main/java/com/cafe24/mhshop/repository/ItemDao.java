package com.cafe24.mhshop.repository;

public interface ItemDao {

	Integer countByCategory(Long categoryNo);	// 카테고리번호에 해당하는 아이템 개수

}
