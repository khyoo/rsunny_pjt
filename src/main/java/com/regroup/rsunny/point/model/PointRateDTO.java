package com.regroup.rsunny.point.model;

import com.regroup.rsunny.common.model.PagingDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PointRateDTO extends PagingDTO {

	private String saleType;	//매물유형
	private String saleTypeNm;	//매물유형
	
	private Double subRateA = 0.0;	// 포인트차감-매매
	private Double subRateB = 0.0;	// 포인트차감-월세
	private Double subRateC = 0.0;	// 포인트차감-전세
	private Double addRateR = 0.0;	// 포인트적립-알써니
	private Double addRateO = 0.0;	// 포인트적립-소유자
	private Double addRateM = 0.0;	// 포인트적립-관리자

}
