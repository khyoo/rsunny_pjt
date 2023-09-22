package com.regroup.rsunny.point.service;

import java.util.List;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.point.model.ItemDTO;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;

public interface PointService {

	List<PointDTO> getList(PointDTO form);

	List<PointDTO> getMyList(PointDTO form);
	
	PointDTO getPoint(PointDTO form);
	
	ResultDTO deposit(PointDTO form);
	
	ResultDTO withdrawal(PointDTO form);
	
	//포인트 적립/차감 비율 목록
	List<PointRateDTO> getPointRate();
	
	ResultDTO savePointRate(PointRateDTO form);
	
	//상태처리
	ResultDTO approve(PointDTO form);

	//아이템목록
	List<ItemDTO> getItemList(ItemDTO form);

	//아이템구매
	ResultDTO buyItem(PointDTO form, ItemDTO iform);

	//아이템사용
	ResultDTO useItem(ItemDTO iform);
	
	//대기중 포인트 상태처리
	ResultDTO processPoint();

}
