package com.cafe24.mhshop.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.cafe24.mhshop.vo.BasketVo;

public class RequestBasketAddGuestDto {
	@NotNull
	private Long optionNo;
	@NotEmpty
	private String guestSesstion;
	@NotNull
	@Min(1)
	private Long cnt;

	public BasketVo toVo() {
		return new BasketVo(null, optionNo, null, guestSesstion, null, cnt, null, null, null, null, null);
	}
	public Long getOptionNo() {
		return optionNo;
	}
	public void setOptionNo(Long optionNo) {
		this.optionNo = optionNo;
	}
	public String getGuestSesstion() {
		return guestSesstion;
	}
	public void setGuestSesstion(String guestSesstion) {
		this.guestSesstion = guestSesstion;
	}
	public Long getCnt() {
		return cnt;
	}
	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}

	
}