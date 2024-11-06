package com.regroup.rsunny.front.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.regroup.rsunny.board.model.BoardDTO;
import com.regroup.rsunny.board.service.BoardService;
import com.regroup.rsunny.common.model.CodeDTO;
import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.common.service.CommonCodeService;
import com.regroup.rsunny.forsale.service.ForsaleService;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.AES256Util;
import com.regroup.rsunny.utils.ForsaleUtil;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FrontPageController {
	
	@Autowired
	private CommonCodeService codeService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private ForsaleService saleService;

	@Autowired
	private UserService userService;
	
	@Value("${rsunny.item.price1}")
	private Long ITEM1_PRICE;
	
	@Value("${rsunny.item.price2}")
	private Long ITEM2_PRICE;
	
	//메인
    @GetMapping(value={"", "/", "/main"})
    public String root(SaleSearchDTO form, Model model) {
    	model.addAttribute("saleTypeList", codeService.getList("SALE_TYPE"));
    	
    	form.setRows(50);
    	if( !StringUtils.isEmpty(form.getSaleDetailType()) ) form.setSaleDetailTypeList(Arrays.asList(form.getSaleDetailType().split(",")));
    	if( !StringUtils.isEmpty(form.getTradeType()) ) form.setTradeTypeList(Arrays.asList(form.getTradeType().split(",")));
       	
       	List<SaleDTO> list = saleService.getList(form);
       	for(SaleDTO item : list) {
       		String linkUrl = String.format("/02la01?t=%s&rid=%s&w=%s", item.getSaleType(), item.getRid(), item.getAddr1());
       		item.setLinkUrl(linkUrl);
       		item.setPriceWon(NumberUtil.priceToStr(item));
			String areaKind = ForsaleUtil.getAreaKind(item);
       		item.setArea(("B".equals(areaKind))? item.getLandArea() : item.getSupplyArea());
       	}
       	
		List<Integer> integers = IntStream.range(0, list.size()).boxed().collect(Collectors.toList());
		Collections.shuffle(integers);
    	
		List<SaleDTO> random10 = new ArrayList<SaleDTO>();
		for(Integer i : integers) {
			random10.add(list.get(i));
			if(random10.size() >= 10) break;
		}
		
       	//model.addAttribute("form", form);
       	model.addAttribute("list", random10);
    	
        return "front/main";
    }
	
    //로그인
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

	//logout
    @GetMapping("/user/logout")
	public String checkLogin(UserDTO form) throws Exception {
		log.info("Check logout...{}", form.getUserid());
		
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        
		return "redirect:/";
	}
	
    @GetMapping("/front/{page}")
    public String front(@PathVariable String page, Model model) {

        return String.format("front/%s", page);
    }
	
    @GetMapping("/{page}")
    public String page(@PathVariable String page, Model model) {

        return String.format("front/%s", page);
    }
	
    //매물검색.
    @GetMapping("/02la01")
    public String forSale(SaleSearchDTO form, Model model) {
    	//매물유형이 빈 값인 경우 원룸.투룸이 기본.
    	if(StringUtils.isEmpty(form.getT())) form.setT("r");
    	
    	model.addAttribute("form", form);
    	form.setSaleType(form.getT());	//t==saleType
       	
       	List<SaleDTO> list = saleService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	       	
       	List<BoardDTO> bannerList = boardService.getList(BoardDTO.display("banner", "Y"));

       	model.addAttribute("banner", null);
       	if(bannerList.size() > 0) {
    		List<Integer> integers = IntStream.range(0, bannerList.size()).boxed().collect(Collectors.toList());
    		Collections.shuffle(integers);
           	model.addAttribute("banner", bannerList.get(integers.get(0)));
       	}
		
       	CodeDTO code = codeService.getCode(CodeDTO.of("SALE_TYPE", form.getT()));
       	model.addAttribute("saleTypeCd", (code==null)? "all" : code.getCd());
       	model.addAttribute("saleTypeNm", (code==null)? "전체" : code.getNm());
    	//공통코드
    	model.addAttribute("SALE_TYPE"		, codeService.getList("SALE_TYPE"));
    	model.addAttribute("SALE_TYPE_DTL"	, codeService.getList(CodeDTO.of("SALE_TYPE_DTL", "SALE_TYPE", form.getT())));
    	model.addAttribute("MOVEIN"			, codeService.getList("MOVEIN"));	//입주시점.
		model.addAttribute("REPORT_TYPE"	, (SessionUtil.isAgentUser())? codeService.getList("REPORT_TYPE2") : codeService.getList("REPORT_TYPE1"));	//신고유형
    	model.addAttribute("TRADE_TYPE"	, codeService.getList("TRADE_TYPE"));	//거래유형

    	model.addAttribute("ITEM1_PRICE"	, ITEM1_PRICE);	//아이템1 가격
    	model.addAttribute("ITEM2_PRICE"	, ITEM2_PRICE);	//아이템2 가격

    	model.addAttribute("BANK_LIST"		, codeService.getList("BANK_CD"));
    	
    	UserDTO user = userService.getUser(UserDTO.of(SessionUtil.getUserid()));
    	if(user==null) user = new UserDTO();
       	model.addAttribute("user", user);

       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
       	
//       	SaleDTO sale = new SaleDTO();
//       	sale.setTradeType("A");              	
//       	sale.setTradeType("B");       	
//       	sale.setTradeType("C");
       	
        return "front/02la01";
    }
    
	
    //회원가입(약관/회원등록)
    @GetMapping(value={"/05ma01", "/05ma02", "/05mb01","/05mb02"})
    public String user05mb01(UserDTO form, HttpServletRequest request, Model model) {
    	model.addAttribute("form", form);
    	
    	String url = request.getRequestURL().toString();
    	String page = url.substring(url.lastIndexOf("/"));
        return String.format("front/%s", page);
    }
	
    //회원가입(약관/회원등록)완료
    @GetMapping(value={"/05ma03/{userid}", "/05mb03/{userid}"})
    public String user05ma03(@PathVariable String userid, UserDTO form, HttpServletRequest request, Model model) {
    	
    	UserDTO data = new UserDTO();
    	try {
    		form.setUserid(AES256Util.decrypt(userid));
    		data = userService.getUser(form);
    	}
    	catch(Exception e) {
    		log.info("[ERROR] {}", e);
    	}
    	
    	model.addAttribute("form", form);
    	model.addAttribute("data", data);

    	String url = request.getRequestURL().toString().replace("/"+userid, "");
    	String page = url.substring(url.lastIndexOf("/"));
        return String.format("front/%s", page);
    }
	
    //매물등록
    @GetMapping(value={"/register/{pagetype}", "/register/{pagetype}/{rid}"})
    public String register(@PathVariable String pagetype, @PathVariable(required = false) String rid, SaleDTO form, HttpServletRequest request, Model model) {
    	String firstPage = "03f01";
    	
    	if(StringUtils.isEmpty(rid) && firstPage.equals(pagetype)) {
    		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    		rid = String.format("%s%sr", LocalDateTime.now().format(dtf), UUID.randomUUID().toString().replaceAll("-", "").substring(0,10));
    		form.setRid(rid);
    	}
    	
    	SaleDTO data = saleService.getSaleEditing(form);
    	if(data != null) {
    		String userType = SessionUtil.getUserType();
    		//RM: 알써니관리자, SU: 슈퍼관리자가 아니고, 등록자가 아니면 메인 화면으로 이동.
    		if( !("SU".equals(userType) || "RM".equals(userType)) && "N".equals(data.getOwnerYn()) ) {
    			return "redirect:/";
    		}
    	}
    	
    	if(data==null) data = new SaleDTO();
    	
    	if("U".equals(data.getEditMode()) && "03f02".equals(pagetype)) {
    		return String.format("redirect:/register/03f03/%s", rid);
    	}

    	//평형 계산.
    	data.setSupplyAreaPy(NumberUtil.m2ToPy(data.getSupplyArea()));
    	data.setPrivateAreaPy(NumberUtil.m2ToPy(data.getPrivateArea()));
    	data.setLandAreaPy(NumberUtil.m2ToPy(data.getLandArea()));
		data.setBuildingAreaPy(NumberUtil.m2ToPy(data.getBuildingArea()));
		
		data.setPriceWon(NumberUtil.priceToStr(data));

    	model.addAttribute("SALE_TYPE"		, codeService.getList("SALE_TYPE"));
    	model.addAttribute("SALE_TYPE_DTL"	, codeService.getList(CodeDTO.of("SALE_TYPE_DTL", "SALE_TYPE", data.getSaleType())));
    	model.addAttribute("TRADE_TYPE"		, codeService.getList("TRADE_TYPE"));
    	model.addAttribute("ANIMAL"			, codeService.getList("ANIMAL"));
    	model.addAttribute("ROOM"			, codeService.getList("ROOM"));
    	model.addAttribute("BATHROOM"		, codeService.getList("BATHROOM"));
    	model.addAttribute("BROKERAGE_TYPE"	, codeService.getList("BROKERAGE_TYPE"));
    	model.addAttribute("DIRECTION"		, codeService.getList("DIRECTION"));
    	model.addAttribute("ELEVATOR"		, codeService.getList("ELEVATOR"));
    	model.addAttribute("MOVEIN"			, codeService.getList("MOVEIN"));
    	model.addAttribute("PARKING"		, codeService.getList("PARKING"));

    	
    	//포함항목.
    	if("03f03".equals(pagetype)) {
    		List<CodeDTO> codeList = codeService.getList(CodeDTO.of("INCLUDE_ITEM", "SALE_TYPE_DTL", data.getSaleDetailType()));
    		List<String> includeItems = codeList.stream().map(CodeDTO::getNm).collect(Collectors.toList());
    		List<String> savedItems = new ArrayList<String>();
    		//코드에 없는데 등록된 항목은 강제로 추가(사용자 직접 입력값)
    		if( !StringUtils.isEmpty(data.getIncludeItems()) ) {
    			savedItems = Arrays.asList(data.getIncludeItems().split("\\|"));
    			int pos = 0;
    			for(String item : savedItems) {
    				if( !StringUtils.isEmpty(item) && !includeItems.contains(item)) {
    					codeList.add(pos, CodeDTO.of(item, item, 0));
    					pos++;
    				}
    			}
    		}
    		//저장항목에 있으면 Y 그 외는 N.
    		for(CodeDTO item : codeList) {
    			item.setUseYn((savedItems.contains(item.getNm()))? "Y" : "N");
    		}
    		
        	model.addAttribute("INCLUDE_ITEMS", codeList);
    	}
    	//옵션항목.
    	if("03f04".equals(pagetype)) {
    		List<CodeDTO> codeList = codeService.getList(CodeDTO.of("OPTION_ITEM", "SALE_TYPE_DTL", data.getSaleDetailType()));
    		List<String> optionItems = codeList.stream().map(CodeDTO::getNm).collect(Collectors.toList());
    		List<String> savedItems = new ArrayList<String>();
    		//코드에 없는데 등록된 항목은 강제로 추가(사용자 직접 입력값)
    		if( !StringUtils.isEmpty(data.getOptionItems()) ) {
    			savedItems = Arrays.asList(data.getOptionItems().split("\\|"));
    			int pos = 0;
    			for(String item : savedItems) {
    				if( !StringUtils.isEmpty(item) && !optionItems.contains(item)) {
    					codeList.add(pos, CodeDTO.of(item, item, 0));
    					pos++;
    				}
    			}
    		}
    		//저장항목에 있으면 Y 그 외는 N.
    		for(CodeDTO item : codeList) {
    			item.setUseYn((savedItems.contains(item.getNm()))? "Y" : "N");
    		}
    		
        	model.addAttribute("OPTION_ITEMS", codeList);
    	}
    	
    	String pagePrefix = "03f";
    	int pageNum = Integer.valueOf(pagetype.replace(pagePrefix, ""));
    	
    	if(pageNum > 1) {
        	model.addAttribute("prevPage", String.format("/register/%s%02d/%s", pagePrefix, pageNum-1, rid));
    	}
    	if(pageNum < 6) {
        	model.addAttribute("nextPage", String.format("/register/%s%02d/%s", pagePrefix, pageNum+1, rid));
    	}
    	
    	String saveUrl = String.format("/forsale/rest/%s/save/%s", pagetype, rid);
    	model.addAttribute("saveUrl", saveUrl);

    	model.addAttribute("form", form);
    	model.addAttribute("data", data);
    	
    	if( !StringUtils.isEmpty(data.getIncludeItems()) ) {
        	model.addAttribute("includeItems", data.getIncludeItems().split("\\|"));
    	}
    	if( !StringUtils.isEmpty(data.getOptionItems()) ) {
        	model.addAttribute("optionItems", data.getOptionItems().split("\\|"));
    	}
    	
    	//l=토지/건물, h1=(주택/빌라)단독/전원, h3=(주택/빌라)민박/펜션
    	if("l".equals(data.getSaleType()) || "h1".equals(data.getSaleDetailType()) || "h3".equals(data.getSaleDetailType())) {
    		String areaTypeNm = String.format("%s㎡(%s평) / %s㎡(%s평)"
    												, NumberUtil.areaToString(data.getLandArea())
    												, NumberUtil.areaToString(data.getLandAreaPy())
    												, NumberUtil.areaToString(data.getBuildingArea())
    												, NumberUtil.areaToString(data.getBuildingAreaPy()));
    		data.setAreaTypeNm(areaTypeNm);
    	}
    	else {
    		String areaTypeNm = String.format("%s㎡(%s평) / %s㎡(%s평)"
					, NumberUtil.areaToString(data.getSupplyArea())
					, NumberUtil.areaToString(data.getSupplyAreaPy())
					, NumberUtil.areaToString(data.getPrivateArea())
					, NumberUtil.areaToString(data.getPrivateAreaPy()));
    		data.setAreaTypeNm(areaTypeNm);
    	}
    	
        model.addAttribute("pageTitle", ("U".equals(data.getEditMode()))? "매물 관리" : "매물 내놓기");
        model.addAttribute("editMode", ("U".equals(form.getEditMode()))? "수정" : "등록");
        model.addAttribute("finalMode", ("m".equals(form.getT()))? "수정" : "등록");
    	model.addAttribute("userType", SessionUtil.getUserType());
    	
        return String.format("front/%s", pagetype);
    }
	
    //매물등록(포함항목)
    @GetMapping("/register/03f03/includeItems")
    public String includeItems(SaleDTO form, HttpServletRequest request, Model model) {
    	SaleDTO data = saleService.getSaleEditing(form);
    	if(data==null) data = new SaleDTO();

    	List<CodeDTO> codeList = codeService.getList(CodeDTO.of("INCLUDE_ITEM", "SALE_TYPE_DTL", form.getSaleDetailType()));
		List<String> includeItems = codeList.stream().map(CodeDTO::getNm).collect(Collectors.toList());
		List<String> savedItems = new ArrayList<String>();
		//코드에 없는데 등록된 항목은 강제로 추가(사용자 직접 입력값)
		if( !StringUtils.isEmpty(data.getIncludeItems()) ) {
			savedItems = Arrays.asList(data.getIncludeItems().split("\\|"));
			int pos = 0;
			for(String item : savedItems) {
				if( !StringUtils.isEmpty(item) && !includeItems.contains(item)) {
					codeList.add(pos, CodeDTO.of(item, item, 0));
					pos++;
				}
			}
		}
		//저장항목에 있으면 Y 그 외는 N.
		for(CodeDTO item : codeList) {
			item.setUseYn((savedItems.contains(item.getNm()))? "Y" : "N");
		}
		
    	model.addAttribute("INCLUDE_ITEMS", codeList);
    	
        return "front/03f03.includeItems";
    }
	
    //회원가입(약관/회원등록)
    @GetMapping(value={"/06u0201"})
    public String faq(BoardDTO form, HttpServletRequest request, Model model) {
    	model.addAttribute("form", form);
    	
    	form.setRows(1000);
    	form.setBtype("faq");
    	

       	List<BoardDTO> list = boardService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
    	
    	String url = request.getRequestURL().toString();
    	String page = url.substring(url.lastIndexOf("/"));
        return String.format("front/%s", page);
    }
	
    //회원가입(약관/회원등록)
    @GetMapping(value={"/06ma01/{userToken}"})
    public String changePasswd(@PathVariable String userToken, UserDTO form, HttpServletRequest request, Model model) {
    	log.info("Change.password ueerToken={}", userToken);
    	
    	UserDTO user = null;
    	
    	try {
        	String userid = AES256Util.decrypt(userToken);
        	form.setUserid(userid);
        	user = userService.getUser(form);
    	}
    	catch(Exception e) {
    		log.error("[ERROR] {}", e);
    	}
    	
    	
    	model.addAttribute("form", form);
    	model.addAttribute("user", user);
    	

        return "front/06ma01";
    }
    
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

}
