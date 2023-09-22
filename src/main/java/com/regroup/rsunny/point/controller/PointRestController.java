package com.regroup.rsunny.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.point.model.ItemDTO;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;
import com.regroup.rsunny.point.service.PointService;
import com.regroup.rsunny.utils.SessionUtil;

import groovyjarjarpicocli.CommandLine.Model;

@RequestMapping("/point/rest")
@RestController
public class PointRestController { 

	@Autowired
	private PointService service;
	
	@Value("${rsunny.item.price1}")
	private Long ITEM1_PRICE;
	
	@Value("${rsunny.item.price2}")
	private Long ITEM2_PRICE;

	/**
	 * 포인트 충전 신청
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/deposit")
	public ResultDTO deposit(PointDTO form, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setInoutType("I");
		form.setInoutDetail("ID");		//충전
		form.setStatus("10");			//신청
		form.setUserid(SessionUtil.getUserid());
		form.setSessionId(SessionUtil.getUserid());
		
		return service.deposit(form);
	}

	/**
	 * 포인트 출금 신청
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/withdrawal")
	public ResultDTO withdrawal(PointDTO form, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setInoutType("O");
		form.setInoutDetail("OW");	//출금
		form.setStatus("50");			//신청
		form.setUserid(SessionUtil.getUserid());
		form.setSessionId(SessionUtil.getUserid());
		
		return service.withdrawal(form);
	}

	/**
	 * 아이템구매(포인트사용)
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/buyItem")
	public ResultDTO buyItem(PointDTO form, ItemDTO iform, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		long point = ("1".equals(iform.getItemType()))? ITEM1_PRICE * iform.getItemCount() : ITEM2_PRICE;
		form.setPoint(point);
		form.setInoutType("O");		//포인트사용
		form.setInoutDetail("OI");	//아이템구매
		form.setStatus("00");		//처리완료
		form.setUserid(SessionUtil.getUserid());
		form.setSessionId(SessionUtil.getUserid());
		
		iform.setInoutType("I");	//아이템구매.
		iform.setStatus("00");
		iform.setUserid(SessionUtil.getUserid());
		iform.setSessionId(SessionUtil.getUserid());
		
		return service.buyItem(form, iform);
	}

	/**
	 * 아이템 사용.
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/useItem")
	public ResultDTO useItem(ItemDTO iform, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		iform.setItemType("1");
		iform.setInoutType("O");	//아이템사용.
		iform.setItemCount(1L);
		iform.setStatus("00");
		iform.setUserid(SessionUtil.getUserid());
		iform.setSessionId(SessionUtil.getUserid());
		
		return service.useItem(iform);
	}

	/**
	 * 포인트 누적/차감율 저장
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/pointRate")
	public ResultDTO pointRate(PointRateDTO form, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}

		form.setSessionId(SessionUtil.getUserid());
		
		return service.savePointRate(form);
	}

	/**
	 * 포인트 승인 처리
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/approve")
	public ResultDTO approve(PointDTO form, Model model) throws Exception {
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setStatus("00");			//처리완료(승인)
		form.setUserid(SessionUtil.getUserid());
		form.setSessionId(SessionUtil.getUserid());
		
		return service.approve(form);
	}

}
