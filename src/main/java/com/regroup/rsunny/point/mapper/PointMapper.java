package com.regroup.rsunny.point.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.forsale.model.ReportDTO;
import com.regroup.rsunny.point.model.ItemDTO;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;

@Mapper
public interface PointMapper {

	List<PointDTO> getList(PointDTO form);
	
	PointDTO getPoint(PointDTO form);
	
	//사용자가 오픈한 매물에 의해 차감된 포인트 정보 조회.
	PointDTO getPointByRid(SaleDTO form);
	
	int insertPoint(PointDTO form);
	
	PointDTO getLastPidByUser(PointDTO form);
	
	//포인트 적립/차감 비율 목록
	List<PointRateDTO> getPointRate();
	
	int insertPointRate(PointRateDTO form);
	
	int updatePointRate(PointRateDTO form);
	
	//상태처리.
	int updateApprove(PointDTO form);

	List<ItemDTO> getItemList(ItemDTO form);
	
	//아이템구매
	int insertItem(ItemDTO form);
	
	//포인트 상태 처리.
	int updatePointStatus();
	
	//아이템2 구매여부 확인
	ItemDTO getItem2Purchase(ItemDTO form);

	//포인트 그룹에 의한 목록 조회(알선하기에 의한 출금예정 & 적립예정 목록)
	List<PointDTO> getPointListByGrp(ReportDTO form);
	
	//포인트 그룹에 의한 목록 삭제(알선하기에 의한 출금예정 & 적립예정 목록)
	int deletePointByGrp(ReportDTO form);

}
