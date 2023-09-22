package com.regroup.rsunny.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustCompanyDTO {

	private Long custCompId;		// 업체ID
	private String custCompNm;		// 업체명
	private String bizNo;			// 사업자번호
	private String addr;			// 주소
	
}
