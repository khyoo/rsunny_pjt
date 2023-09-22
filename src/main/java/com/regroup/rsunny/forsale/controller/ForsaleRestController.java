package com.regroup.rsunny.forsale.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.forsale.model.PositionDTO;
import com.regroup.rsunny.forsale.model.ReportDTO;
import com.regroup.rsunny.forsale.service.ForsaleService;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.SessionUtil;

import groovyjarjarpicocli.CommandLine.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/forsale/rest")
@RestController
public class ForsaleRestController { 

	@Autowired
	private ForsaleService service;

	@Autowired
	private UserService userService;

	/**
	 * 매물 조회
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/getSalePositions")
	public Map<String, List<PositionDTO>> getSalePositions(SaleSearchDTO form, Model model) throws Exception {
		//한 페이지에 검색되도록 충분히 설정.
		form.setRows(100000);
		
    	if( !StringUtils.isEmpty(form.getSaleDetailType()) ) form.setSaleDetailTypeList(Arrays.asList(form.getSaleDetailType().split(",")));
    	if( !StringUtils.isEmpty(form.getTradeType()) ) form.setTradeTypeList(Arrays.asList(form.getTradeType().split(",")));
       	
		
		List<SaleDTO> list = service.getList(form);
		
		List<PositionDTO> positions = new ArrayList<PositionDTO>();
		list.forEach(item -> {
			PositionDTO position = new PositionDTO();
			position.setLat(Double.valueOf(item.getPosY()));
			position.setLng(Double.valueOf(item.getPosX()));
			
			positions.add(position);
		});
		
		Map<String, List<PositionDTO>> result = new HashMap<String, List<PositionDTO>>();
		result.put("positions", positions);
		return result;
	}

	/**
	 * 매물 조회
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/getSale")
	public SaleDTO getSale(SaleDTO form, Model model) throws Exception {
		return service.getSale(form);
	}

	/**
	 * 매물 조회
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/view/{rid}")
	public SaleDTO saleView(@PathVariable String rid, SaleDTO form, Model model) throws Exception {
		log.info(rid);
    	SaleDTO data = service.getSale(form);
		
		//신고시 사용하기 위한 중개사 목록.
    	if(data != null) {
    		form.setRows(1000);
        	List<UserDTO> agentList = service.getAgentList(form);
        	for(UserDTO user: agentList) {
        		user.setPhone( NumberUtil.getFormatPhone(user.getPhone()) );
        	}
        	data.setAgentList(agentList);
        	
        	//LINK URL
        	//http://127.0.0.1:8080/02la01?t=r&r=21122017162073886f88d28c6r&w=%EC%A0%84%EB%82%A8%20%EC%97%AC%EC%88%98%EC%8B%9C%20%EA%B0%80%EA%B3%A1%EA%B8%B8%206
       		String linkUrl = String.format("%s/02la01?t=%s&rid=%s&w=%s", SessionUtil.getDomainUrl(), data.getSaleType(), data.getRid(), URLEncoder.encode(data.getAddr1(),"UTF-8"));
       		data.setLinkUrl(linkUrl);
    	}

		return data;
	}

	/**
	 * 매물 저장
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{pagetype}/save/{rid}")
	public ResultDTO save(@PathVariable String pagetype, @PathVariable String rid, SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/save/{}", pagetype, rid);
		
		if( !SessionUtil.isAuthenticated() ) {
			return ResultDTO.of(-1, "로그인후 등록해 주세요.");
		}
		
		form.setSessionId(SessionUtil.getUserid());
		
		if("03f02".equals(pagetype)) {
			return service.saveSale02(form);
		}
		else if("03f03".equals(pagetype)) {
			return service.saveSale03(form);
		}
		else if("03f04".equals(pagetype)) {
			return service.saveSale04(form);
		}
		else if("03f05".equals(pagetype)) {
			ResultDTO result = service.saveSale05(form);
			if(result.getCode() != 0) return result;
			//최종 상태 업데이트.
			//03f06 페이지 처리 내용으로 이리 이동함.
			return service.updateFinalStatus(form);
		}
		else if("03f06".equals(pagetype)) {		//미사용(바로 홈으로 이동하도록 수정함).
			return service.updateFinalStatus(form);
		}
		else {
			return ResultDTO.of(-1, "잘못된 정보입니다.");
		}
	}

	/**
	 * 매물 삭제
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/delete")
	public ResultDTO delete(SaleDTO form, Model model) throws Exception {
		log.info("/forsale/rest/delete....{}", form.getRid());
		
		return service.deleteSale(form);
	}

	/**
	 * 매물 삭제
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{pagetype}/delete/{rid}")
	public ResultDTO delete(@PathVariable String pagetype, @PathVariable String rid, SaleDTO form, Model model) throws Exception {
		log.info("/{btype}/rest/delete", pagetype, rid);
		
		return service.deleteSale(form);
	}

	/**
	 * 매물 상태 토글
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/toggleStatus")
	public ResultDTO toggleStatus(SaleDTO form, Model model) throws Exception {
		log.info("/toggleStatus...rid={}", form.getStatus());
		form.setSessionId(SessionUtil.getUserid());
		
		return service.toggleStatus(form);
	}

	/**
	 * 해당 매물에 대한 공인중개사 목록(알선받기)
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/getAgentList")
	public List<UserDTO> getAgentList(ReportDTO form, Model model) throws Exception {
		
    	form.setRows(1000);
    	form.setSessionId(SessionUtil.getUserid());
    	
    	List<UserDTO> list = service.getAgentList(form);
    	for(UserDTO user: list) {
    		user.setPhone( NumberUtil.getFormatPhone(user.getPhone()) );
    	}
    	
    	return list;
	}

	/**
	 * 알선하기(매물열람) pre-check.
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/checkOffer")
	public ResultDTO checkOffer(SaleDTO form, Model model) throws Exception {
		
    	form.setSessionId(SessionUtil.getUserid());
    	
    	return service.checkOffer(form);
	}

	/**
	 * 알선하기(매물열람)
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/dooffer")
	public ResultDTO dooffer(SaleDTO form, Model model) throws Exception {
		
    	form.setSessionId(SessionUtil.getUserid());
    	
    	return service.doOffer(form);
	}

	/**
	 * 중개사(회원) 정보 조회
	 * @param PositionDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/agent-info")
	public UserDTO agentInfo(UserDTO form, Model model) throws Exception {
		UserDTO data = userService.getUser(form);
		if(data==null) data = UserDTO.of(form.getUserid());

		data.setPasswd(null);
		data.setPhone(NumberUtil.getFormatPhone(data.getPhone()));
		
    	return data;
	}
	
	/**
	 * 실거래 데이터 좌표(posX, posY) 업데이트
	 * @param SaleDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/tradings/updateCoords")
	public ResultDTO updateRealTradings(SaleDTO form, Model model) throws Exception {
		log.info("/rest/tradings/updateCoords");
		
		System.out.println("----------------------");
		
		return service.updateRealTradings(form);
	}
	
	/**
	 * 필지고유번호(pnu)에 의한 실거래 데이터 목록
	 * @param SaleDTO
	 * @return List<SaleDTO>
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListFromPnu")
	public List<SaleDTO> getRealTradingsListFromPnu(SaleDTO form, Model model) throws Exception {
		log.info("/rest/tradings/getListFromPnu");
		
       	SaleDTO data = service.getRealTradingsListFromPnu(form);
       	
       	List<SaleDTO> rtlist = null;
       	
       	if (data != null) {
	       	if (data.getSaleDetailType().equals("a1") && data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsAptListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("a1") && !data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsApt2ListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("a2") && data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsOfficetelListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("a2") && !data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsOfficetel2ListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("a3")) {
	       		rtlist = service.getRealTradingsPresaleListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("h1") && data.getTradeType().equals("A")) {
	       		
	       	} else if (data.getSaleDetailType().equals("h1") && !data.getTradeType().equals("A")) {
	       		
	       	} else if (data.getSaleDetailType().equals("h2") && data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsMultihouseListFromPnu(form);
	       	} else if (data.getSaleDetailType().equals("h2") && !data.getTradeType().equals("A")) {
	       		rtlist = service.getRealTradingsMultihouse2ListFromPnu(form);
	       	}
       	}
		
		return rtlist;
	}
	
	
	/**
	 * 실거래 데이터 아파트 매매
	 * @param SaleDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListApt1")
	public List<SaleDTO> getRealTradingsListFromApt1(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListApt1/{}");
		
       	List<SaleDTO> rtlist = service.getRealTradingsListFromApt1(form);       	
		
		return rtlist;
	}
	
	/**
	 * 실거래 데이터 아파트 전/월세
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListApt2")
	public List<SaleDTO> getRealTradingsListFromApt2(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListApt2/{}");
		
       	List<SaleDTO> rtlist = service.getRealTradingsListFromApt2(form);       	
		
		return rtlist;
	}
	
	/**
	 * 실거래 데이터 오피스텔 매매
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListOfficetel1")
	public List<SaleDTO> getRealTradingsListFromOfficetel1(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListOfficetel1/{}");
		System.out.println(form.getSidoCode());
       	//List<SaleDTO> rtlist = service.getRealTradingsListFromOfficetel1(form);
		
		List<SaleDTO> rtlist = null;
	
		switch(form.getSidoCode()) {
			case "11"://서울
				rtlist = service.getRealTradingsSeoulList(form);
				break;
			case "41"://경기
				rtlist = service.getRealTradingsGyeonggiList(form);
				break;
			case "28"://인천
				rtlist = service.getRealTradingsIncheonList(form);
				break;
			
			
			case "27"://대구
				rtlist = service.getRealTradingsDaeguList(form);
				break;
			case "30"://대전
				rtlist = service.getRealTradingsDaejeonList(form);
				break;
			case "29"://광주
				rtlist = service.getRealTradingsGwangjuList(form);
				break;
			case "26"://부산
				rtlist = service.getRealTradingsBusanList(form);
				break;
			case "31"://울산
				rtlist = service.getRealTradingsUlsanList(form);
				break;
			case "36"://세종
				rtlist = service.getRealTradingsSejongList(form);
				break;
			case "42"://강원
				rtlist = service.getRealTradingsGangwonList(form);
				break;
			case "50"://제주
				rtlist = service.getRealTradingsJejuList(form);
				break;
			case "43"://충북
				rtlist = service.getRealTradingsChungcheongbukdoList(form);
				break;
			case "44"://충남
				rtlist = service.getRealTradingsChungcheongnamdoList(form);
				break;
			
			
			case "45"://전북
				rtlist = service.getRealTradingsJeollabukdoList(form);
				break;
			case "46"://전남
				rtlist = service.getRealTradingsJeollanamdoList(form);
				break;
			case "47"://경북
				rtlist = service.getRealTradingsGyeongsangbukdoList(form);
				break;
			case "48"://경남
				rtlist = service.getRealTradingsGyeongsangnamdoList(form);
				break;
				
		}
		
		return rtlist;
	}
	
	/**
	 * 실거래 데이터 오피스텔 전월세
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListOfficetel2")
	public List<SaleDTO> getRealTradingsListFromOfficetel2(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListOfficetel2/{}");
		System.out.println(form.getSidoCode());
       	List<SaleDTO> rtlist = service.getRealTradingsListFromOfficetel2(form);       	
		
		return rtlist;
	}
	
	/**
	 * 실거래 데이터 연립다세대 매매
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListMultihouse1")
	public List<SaleDTO> getRealTradingsListFromMultihouse1(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListMultihouse1/{}");
		
       	List<SaleDTO> rtlist = service.getRealTradingsListFromMultihouse1(form);       	
		
		return rtlist;
	}
	
	/**
	 * 실거래 데이터 연립다세대 전월세
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@GetMapping("/tradings/getListMultihouse2")
	public List<SaleDTO> getRealTradingsListFromMultihouse2(SaleDTO form, Model model) throws Exception {
		log.info("/rest/{}/tradings/getListApt3/{}");
		
       	List<SaleDTO> rtlist = service.getRealTradingsListFromMultihouse2(form);       	
		
		return rtlist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 실거래 데이터 수집
	 * @param RealTradingsDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/tradings/test/insert")
	public ResultDTO insertTestRealTradings(SaleDTO form, Model model) throws Exception {
		log.info("/rest/tradings/test/insert/");
		
		System.out.println("----------------------");
		
		// 추후 return 할 데이터 목록
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
	    // return data key 목록
	    List<String> headerList = new ArrayList<String>();
	    
		try{
	    	BufferedReader br = Files.newBufferedReader(Paths.get("d:/test1.csv"));
	        String line = "";
	        
	        while((line = br.readLine()) != null){
				List<String> stringList = new ArrayList<String>();
				String stringArray[] = line.split(",");
				stringList = Arrays.asList(stringArray);
	            
				// csv 1열 데이터를 header로 인식
				if(headerList.size() == 0){
					headerList = stringList;
				} else {
	 				Map<String, Object> map = new HashMap<String, Object>();
					// header 컬럼 개수를 기준으로 데이터 set
					for(int i = 0; i < headerList.size(); i++){
						map.put(headerList.get(i), stringList.get(i));
					}
					mapList.add(map);
				}
			}
	        System.out.println(mapList);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		
		return service.insertTest(form);
	}
	
	
}
