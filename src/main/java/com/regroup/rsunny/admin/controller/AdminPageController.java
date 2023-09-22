package com.regroup.rsunny.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.regroup.rsunny.board.model.BoardDTO;
import com.regroup.rsunny.board.service.BoardService;
import com.regroup.rsunny.common.model.CodeDTO;
import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.common.service.CommonCodeService;
import com.regroup.rsunny.forsale.model.ReportDTO;
import com.regroup.rsunny.forsale.service.ForsaleService;
import com.regroup.rsunny.forsale.service.ReportService;
import com.regroup.rsunny.point.model.ItemDTO;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;
import com.regroup.rsunny.point.service.PointService;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.PagingUtil;

@RequestMapping("/siteadmin")
@Controller
public class AdminPageController { 

	@Autowired
	private CommonCodeService codeService;

	@Autowired
	private UserService userService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private ForsaleService saleService;

	@Autowired
	private PointService pointService;

	@Autowired
	private ReportService reprortService;
	
    @GetMapping(value={"", "/"})
    public String root(Model model) {
        return "redirect:/siteadmin/user/manager";
    }
	
    @GetMapping("/{page}")
    public String admin(@PathVariable String page, Model model) {
        return String.format("admin/%s", page);
    }
	
    @GetMapping("/login")
    public String login(UserDTO form, Model model) {
        return "admin/login";
    }
	
    @GetMapping("/user/{pageType}")
    public String user(@PathVariable String pageType, UserDTO form, Model model) {
    	System.out.println(pageType);
    	System.out.println(form.getPageType());
       	List<UserDTO> list = userService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("branchList"	, userService.getBranchList() );

       	model.addAttribute("form", form);
       	model.addAttribute("list", list);
		//코드 조회.
       	List<String> filter = Arrays.asList("SU", "RM", "BM");
       	model.addAttribute("userTypeList"	, codeService.getList("USER_TYPE").stream().filter(item -> filter.contains(item.getCd())).collect(Collectors.toList()) );
       	
        return String.format("admin/user-%s", pageType);
    }
	
    @GetMapping("/sale")
    public String sale(SaleSearchDTO form, Model model) {
		//코드 조회.
       	model.addAttribute("saleTypeList"	, codeService.getList("SALE_TYPE"));
       	model.addAttribute("tradeTypeList"	, codeService.getList("TRADE_TYPE"));
       	model.addAttribute("postLList"		, codeService.getList("POST_L"));
       	model.addAttribute("postMList"		, codeService.getList(CodeDTO.of("POST_M", "POST_L", form.getPostL())));
       	model.addAttribute("postSList"		, codeService.getList(CodeDTO.of("POST_S", "POST_M", form.getPostM())));
       	
       	List<SaleDTO> list = saleService.getListForAdmin(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	for(SaleDTO item : list) {
       		String linkUrl = String.format("/02la01?t=%s&rid=%s&w=%s", item.getSaleType(), item.getRid(), item.getAddr1());
       		item.setLinkUrl(linkUrl);
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/sale";
    }
	
    @GetMapping("/report")
    public String report(ReportDTO form, Model model) {
		//코드 조회.
       	model.addAttribute("userTypeList"	, codeService.getList("USER_TYPE") );

       	form.setPageType("FORSALE");
       	List<ReportDTO> list = reprortService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/report";
    }
	
    @GetMapping("/report-agent")
    public String reportAgent(ReportDTO form, Model model) {
		//코드 조회.
       	model.addAttribute("userTypeList"	, codeService.getList("USER_TYPE") );

       	form.setPageType("AGENT");
       	List<ReportDTO> list = reprortService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/report-agent";
    }
	
    @GetMapping("/point-rate")
    public String pointRate(PointDTO form, Model model) {
    	//포인트 적립/차감 비율 목록
      	List<PointRateDTO> rateList = pointService.getPointRate();
       	model.addAttribute("rateList", rateList);

       	List<PointDTO> list = pointService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/point-rate";
    }
	
    @GetMapping("/point-list")
    public String pointList(PointDTO form, Model model) {
		//코드 조회.
       	model.addAttribute("pointTypeList"	, codeService.getList("POINT_TYPE_DTL"));
       	model.addAttribute("pointStatusList", codeService.getList("POINT_STATUS"));
       	model.addAttribute("userTypeList"	, codeService.getList("USER_TYPE") );

       	List<PointDTO> list = pointService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/point-list";
    }
	
    @GetMapping("/point/view")
    public String pointView(PointDTO form, Model model) {

    	PointDTO data = pointService.getPoint(form);
       	model.addAttribute("data", data);

        return "admin/point-view";
    }
	
    @GetMapping("/ad-item")
    public String adItem(ItemDTO form, Model model) {
		//코드 조회.
       	model.addAttribute("userTypeList"	, codeService.getList("USER_TYPE") );

       	List<ItemDTO> list = pointService.getItemList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/ad-item";
    }
	
    @GetMapping("/board/{btype}")
    public String board(@PathVariable String btype, BoardDTO form, Model model) {

       	List<BoardDTO> list = boardService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return String.format("admin/%s", btype);
    }
	
    @GetMapping("/popup/forsale")
    public String userForsale(SaleSearchDTO form, Model model) {

       	List<SaleDTO> list = saleService.getSalesOpenList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.pagingAdmin(form.getTotalCount(), form.getPage(), form.getRows(), "forsalePageCallback"));
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/popup/forsale-list";
    }
	
    @GetMapping("/popup/item")
    public String userItem(ItemDTO form, Model model) {

       	model.addAttribute("useAvailItem", 0);
       	model.addAttribute("useCount", 0);
       	model.addAttribute("applyCount", 0);

       	List<ItemDTO> list = pointService.getItemList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       		ItemDTO item = list.get(0);
           	model.addAttribute("useAvailItem", item.getUseAvailItem());
           	model.addAttribute("useCount", item.getUseCount());
           	model.addAttribute("applyCount", item.getApplyCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.pagingAdmin(form.getTotalCount(), form.getPage(), form.getRows(), "itemPageCallback"));
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/popup/item-list";
    }
	
    @GetMapping("/popup/point")
    public String userPoint(PointDTO form, Model model) {
       	UserDTO user = userService.getUser(UserDTO.of(form.getSessionId()));
       	model.addAttribute("user", user);
    	
       	List<PointDTO> list = pointService.getList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.pagingAdmin(form.getTotalCount(), form.getPage(), form.getRows(), "pointPageCallback"));
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/popup/point-list";
    }
	
    @GetMapping("/popup/agent")
    public String linkedAgent(UserDTO form, Model model) {
       	UserDTO user = userService.getUser(UserDTO.of(form.getSessionId()));
       	model.addAttribute("user", user);
    	
       	List<UserDTO> list = userService.getLinkedAgentList(form);
       	if(list.size() > 0) {
       		form.setTotalCount(list.get(0).getTotalCount());
       	}
       	//페이징 HTML.
       	form.setPagingHtml(PagingUtil.pagingAdmin(form.getTotalCount(), form.getPage(), form.getRows(), "agentPageCallback"));
       	
       	model.addAttribute("form", form);
       	model.addAttribute("list", list);

        return "admin/popup/agent-list";
    }

}
