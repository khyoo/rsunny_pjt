package com.regroup.rsunny.forsale.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.regroup.rsunny.common.model.CodeDTO;
import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.common.service.CommonCodeService;
import com.regroup.rsunny.forsale.service.ForsaleService;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.service.PointService;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.PagingUtil;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/forsale")
public class ForsalePageController {

	@Autowired
	private ForsaleService saleService;

	@Autowired
	private PointService pointService;
	
	@Autowired
	private CommonCodeService codeService;
	
    //매물검색.
    @GetMapping("/tab1")
    public String saleList(SaleSearchDTO form, Model model) {
    	if( !StringUtils.isEmpty(form.getSaleDetailType()) ) form.setSaleDetailTypeList(Arrays.asList(form.getSaleDetailType().split(",")));
    	if( !StringUtils.isEmpty(form.getTradeType()) ) form.setTradeTypeList(Arrays.asList(form.getTradeType().split(",")));

    	form.setRows(15);
       	
       	List<SaleDTO> list = saleService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.paging(form.getTotalCount(), form.getPage(), form.getRows(), "movePageTab1"));
       	
       	CodeDTO code = codeService.getCode(CodeDTO.of("SALE_TYPE", form.getSaleType()));
       	model.addAttribute("saleTypeNm", (code==null)? "전체" : code.getNm());
    	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
       	
       	model.addAttribute("clusterOneItem", (form.getPage()==1 && form.getSwX() != null && list.size()==1));
       	log.info("{}", model.getAttribute("clusterOneItem"));
    	
        return "front/02la01.tab1";
    }
	
    //등록매물.
    @GetMapping("/tab2")
    public String myList(SaleSearchDTO form, Model model) {
    	//매물유형이 빈 값인 경우 원룸.투룸이 기본.
    	//if(StringUtils.isEmpty(form.getT())) form.setT("r");
    	
    	//model.addAttribute("form", form);
    	//form.setSaleType(form.getT());	//t==saleType

    	form.setRows(15);
     	
    	//현재 로그인한 사용자의 매물.
    	form.setSessionId(SessionUtil.getUserid());
    	form.setStatus("");
       	
       	List<SaleDTO> list = saleService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.paging(form.getTotalCount(), form.getPage(), form.getRows(), "movePageTab2"));
       	
       	CodeDTO code = codeService.getCode(CodeDTO.of("SALE_TYPE", form.getSaleType()));
       	model.addAttribute("saleTypeNm", (code==null)? "전체" : code.getNm());
    	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
    	
        return "front/02la01.tab2";
    }
	
    //포인트 목록
    @GetMapping("/tab4")
    public String pointList(PointDTO form, Model model) {
    	form.setRows(5);
    	form.setSessionId(SessionUtil.getUserid());
    	
       	List<PointDTO> list = pointService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.paging(form.getTotalCount(), form.getPage(), form.getRows(), "pointList"));
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
    	
        return "front/02la01.tab4";
    }
	
    //포인트 목록
    @GetMapping("/agentList")
    public String agentList(SaleDTO form, Model model) {
    	
    	//System.out.println("agent-list");
    	form.setRows(1000);
    	form.setSessionId(SessionUtil.getUserid());
    	
    	List<UserDTO> list = saleService.getAgentList(form);
    	for(UserDTO user: list) {
    		user.setPhone( NumberUtil.getFormatPhone(user.getPhone()) );
    	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
    	
        return "front/02la01.offer-agent";
    }

}
