package com.regroup.rsunny.point.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.point.mapper.PointMapper;
import com.regroup.rsunny.point.model.ItemDTO;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;
import com.regroup.rsunny.point.service.PointService;
import com.regroup.rsunny.system.mapper.UserMapper;
import com.regroup.rsunny.system.model.UserDTO;

@Service
public class PointServiceImpl implements PointService {
	@Autowired
    private PointMapper mapper;

	@Autowired
    private UserMapper userMapper;
	
	@Value("${rsunny.item.price1}")
	private Long ITEM1_PRICE;
	
	@Value("${rsunny.item.price2}")
	private Long ITEM2_PRICE;

	@Override
	public List<PointDTO> getList(PointDTO form) {
		return mapper.getList(form);
	}

	@Override
	public List<PointDTO> getMyList(PointDTO form) {
		return mapper.getList(form);
	}
	
	@Override
	public PointDTO getPoint(PointDTO form) {
		return mapper.getPoint(form);
	}
	
	@Override
	public ResultDTO deposit(PointDTO form) {
		mapper.insertPoint(form);
		return ResultDTO.of(0, "충전 신청되었습니다.");
	}
	
	@Override
	public ResultDTO withdrawal(PointDTO form) {
		UserDTO user = userMapper.getUser(form.getUserid());
		if(user==null) {
			return ResultDTO.of(-1, "사용자 정보가 없습니다.");
		}
		else if(user.getUseAvailPoint() < form.getPoint()) {
			return ResultDTO.of(-99, "출금 가능 포인트를 확인하세요.");
		}
		
		mapper.insertPoint(form);
		return ResultDTO.of(0, "출금 신청되었습니다.");
	}

	@Override
	public List<PointRateDTO> getPointRate() {
		return mapper.getPointRate();
	}
	
	@Override
	public ResultDTO savePointRate(PointRateDTO form) {
		int cnt = mapper.updatePointRate(form);
		if(cnt==0) {
			mapper.insertPointRate(form);
		}
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO approve(PointDTO form) {
		PointDTO data = mapper.getPoint(form);
		if("O".equals(data.getInoutType()) && data.getWithdrawalAvailPoint() < data.getPoint()) {
			return ResultDTO.of(-1, "출금 가능 포인트를 초과하였습니다.");
		}
		
		mapper.updateApprove(form);
		return ResultDTO.of(0, "처리되었습니다.");
	}

	@Override
	public List<ItemDTO> getItemList(ItemDTO form) {
		return mapper.getItemList(form);
	}
	
	@Override
	@Transactional
	public ResultDTO buyItem(PointDTO form, ItemDTO iform) {
		UserDTO user = userMapper.getUser(form.getUserid());
		if(user==null) {
			return ResultDTO.of(-1, "사용자 정보가 없습니다.");
		}
		else if(user.getUseAvailPoint() < form.getPoint()) {
			return ResultDTO.of(-99, "포인트 부족으로 전환 처리 되지 못했습니다.");
		}
		
		String itemType = iform.getItemType();
		if("2".equals(itemType)) {	//아이템2이면 이미 구매했는지 확인.
			ItemDTO item = mapper.getItem2Purchase(iform);
			if(item != null) {
				return ResultDTO.of(-98, String.format("이미 구매하여 적용중인 아이템입니다.\n적용시작일: %s\n적용종료일: %s", item.getStartDtm(), item.getEndDtm()));
			}
		}
		
		//포인트 등록.
		mapper.insertPoint(form);
		
		//아이템 단가 및 합계 가격 계산
		long unitPrice = "1".equals(itemType)? ITEM1_PRICE : ITEM2_PRICE;
		iform.setUnitPrice(unitPrice);
		iform.setItemPrice(unitPrice * iform.getItemCount());
		
		PointDTO lastPoint = mapper.getLastPidByUser(form);	//등록된 포인트ID 조회.
		iform.setPid(lastPoint.getPid());
		iform.setRemark(String.format("ITEM %s 구매", itemType));
		if("2".equals(itemType)) {	//아이템2이면 즉시 적용.
			iform.setInoutType("IO"); 	//구매&적용.
			iform.setStartDtm(LocalDateTime.now());
			iform.setEndDtm(LocalDate.now().plusDays(10).atTime(23, 59, 59));
		}
		
		//아이템 등록.
		mapper.insertItem(iform);
		
		return ResultDTO.of(0, "광고 아이템 전환이 완료 되었습니다. ");
	}
	
	@Override
	@Transactional
	public ResultDTO useItem(ItemDTO form) {
		UserDTO user = userMapper.getUser(form.getUserid());
		if(user==null) {
			return ResultDTO.of(-1, "사용자 정보가 없습니다.");
		}
		else if(user.getUseAvailItem() < 1) {
			return ResultDTO.of(-99, "적용 가능한 아이템이 없습니다. 아이템 구매후 적용하세요.");
		}
		
		//아이템 단가 및 합계 가격 계산
		long unitPrice = "1".equals(form.getItemType())? ITEM1_PRICE : ITEM2_PRICE;
		form.setUnitPrice(unitPrice);
		form.setItemPrice(unitPrice * form.getItemCount());
		
		form.setRemark(String.format("ITEM 1 사용", form.getItemType()));
		form.setStartDtm(LocalDateTime.now());
		form.setEndDtm(LocalDate.now().plusDays(10).atTime(23, 59, 59));
		
		//아이템 등록.
		mapper.insertItem(form);
		
		return ResultDTO.of(0, "광고 아이템 적용이 완료 되었습니다. ");
	}
	
	@Override
	public ResultDTO processPoint() {
		mapper.updatePointStatus();
		return ResultDTO.of(0, "처리되었습니다.");
	}
	
}
