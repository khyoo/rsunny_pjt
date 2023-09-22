package com.regroup.rsunny.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustManagerDTO {

	private Long custMngrId;		// 업체담당자ID
	private String custMngrNm;		// 업체담당자명
	private Long custCompId;		// 업체ID
	private String job;				// 직책
	private String phone;			// 연락처
	private String email;			// 이데일

}
