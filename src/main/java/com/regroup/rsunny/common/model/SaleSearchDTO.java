package com.regroup.rsunny.common.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleSearchDTO extends SaleDTO {

	private List<String> saleDetailTypeList = new ArrayList<String>();
	private List<String> tradeTypeList = new ArrayList<String>();

	//매매·보증·전세금
	private String priceFr;
	private String priceTo;
	//월세
	private String monthlyPriceFr;
	private String monthlyPriceTo;
	//전용면적
	private String privateAreaFr;
	private String privateAreaTo;
	//준공년월
	private String completionYearFr;
	private String completionYearTo;
	//입주시점
	private String moveinType;
	//ActiveTab
	private String active;
	//Status(00은 노출. 기본은 노출만 조회)
	private String status = "00";
	
	private Double swX;	//남서 좌표
	private Double swY; //남서 좌표
	private Double neX;	//북동 좌표
	private Double neY;	//북동 좌표
	
	//지역
	private String sido;					//광역시도
	private String sigungu;					//시도군
	private String dong;					//동(법정동)
	
	private String userType;				//사용자유형.
	private String typeSessionId;			//사용자유형용 사용자 로그인 ID.
	
}
