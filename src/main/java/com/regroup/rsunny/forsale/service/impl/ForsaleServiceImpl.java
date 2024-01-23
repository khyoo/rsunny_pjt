package com.regroup.rsunny.forsale.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.common.model.SaleDTO;
import com.regroup.rsunny.common.model.SaleSearchDTO;
import com.regroup.rsunny.forsale.mapper.ForsaleMapper;
import com.regroup.rsunny.forsale.model.address.Address;
import com.regroup.rsunny.forsale.model.address.DaumAddressDTO;
import com.regroup.rsunny.forsale.model.address.JibunAddress;
import com.regroup.rsunny.forsale.model.address.RoadAddress;
import com.regroup.rsunny.forsale.service.ForsaleService;
import com.regroup.rsunny.point.mapper.PointMapper;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.point.model.PointRateDTO;
import com.regroup.rsunny.system.mapper.UserMapper;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.utils.DateUtil;
import com.regroup.rsunny.utils.DaumAddressUtil;
import com.regroup.rsunny.utils.ForsaleUtil;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.ObjectMapperUtil;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ForsaleServiceImpl implements ForsaleService {
	@Autowired
    private ForsaleMapper mapper;

	@Autowired
    private UserMapper userMapper;

	@Autowired
    private PointMapper pointMapper;
	
	@Value("${rsunny.file.upload-path}")
	private String UPLOAD_BASE_PATH;
	
	@Value("${rsunny.file.upload-folder}")
	private String UPLOAD_BASE_FOLDER;

	@Value("${rsunny.daum.restapi-key}")
	private String DAUM_RESTAPI_KEY;

	@Value("${rsunny.rsunnyid}")
	private String RSUNNY_ID;


	@Override
	public List<SaleDTO> getList(SaleSearchDTO form) {
		//공인중개사/임대관리자인 경우 단독중개를 목록에서 빼기 위해 사용자 유형을 조건에 설정.
		form.setUserType(SessionUtil.getUserType());
		form.setTypeSessionId(SessionUtil.getUserid());
		
		List<SaleDTO> list = mapper.getList(form);
		list.forEach(item -> {
			//면적종류
			item.setAreaKind(ForsaleUtil.getAreaKind(item));
			//평형
			item.setSupplyAreaPy(NumberUtil.m2ToPy(item.getSupplyArea()));
			item.setPrivateAreaPy(NumberUtil.m2ToPy(item.getPrivateArea()));
			item.setLandAreaPy(NumberUtil.m2ToPy(item.getLandArea()));
			item.setBuildingAreaPy(NumberUtil.m2ToPy(item.getBuildingArea()));
			//면적(평형)
			item.setSupplyAreaWithPy(NumberUtil.m2WithPy(item.getSupplyArea()));
			item.setPrivateAreaWithPy(NumberUtil.m2WithPy(item.getPrivateArea()));
			item.setLandAreaWithPy(NumberUtil.m2WithPy(item.getLandArea()));
			item.setBuildingAreaWithPy(NumberUtil.m2WithPy(item.getBuildingArea()));

			//편집URL
			item.setEditUrl(String.format("/register/03f03/%s", item.getRid()));
			//가격
			item.setPriceWon(NumberUtil.priceToStr(item));
			//관리비
			item.setMaintenanceFeeWon(("Y".equals(item.getMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));
			item.setShortMaintenanceFeeWon(("Y".equals(item.getShortMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));	//단기 관리비
			
			item.setOwnerYn(item.getCreateId()!=null && item.getCreateId().equals(SessionUtil.getUserid())? "Y" : "N");	//매물등록자인지 여부.
		});
		return list;
	}

	@Override
	public List<SaleDTO> getListForAdmin(SaleSearchDTO form) {
		List<SaleDTO> list = mapper.getListForAdmin(form);;
		list.forEach(item -> {
			//면적종류
			item.setAreaKind(ForsaleUtil.getAreaKind(item));
			//평형
			item.setSupplyAreaPy(NumberUtil.m2ToPy(item.getSupplyArea()));
			item.setPrivateAreaPy(NumberUtil.m2ToPy(item.getPrivateArea()));
			item.setLandAreaPy(NumberUtil.m2ToPy(item.getLandArea()));
			item.setBuildingAreaPy(NumberUtil.m2ToPy(item.getBuildingArea()));
			//면적(평형)
			item.setSupplyAreaWithPy(NumberUtil.m2WithPy(item.getSupplyArea()));
			item.setPrivateAreaWithPy(NumberUtil.m2WithPy(item.getPrivateArea()));
			item.setLandAreaWithPy(NumberUtil.m2WithPy(item.getLandArea()));
			item.setBuildingAreaWithPy(NumberUtil.m2WithPy(item.getBuildingArea()));
			//편집URL
			item.setEditUrl(String.format("/register/03f03/%s", item.getRid()));
			//가격
			item.setPriceWon(NumberUtil.priceToStr(item));
			//관리비
			item.setMaintenanceFeeWon(("Y".equals(item.getMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));
			item.setShortMaintenanceFeeWon(("Y".equals(item.getShortMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));	//단기 관리비
		});
		return list;
	}

	@Override
	public List<SaleDTO> getSalesOpenList(SaleSearchDTO form) {
		List<SaleDTO> list = mapper.getSalesOpenList(form);;
		return list;
	}

	@Override
	public List<SaleDTO> getMyList(SaleSearchDTO form) {
		//현재 사용자가 등록한 목록만 조회.
		form.setSessionId(SessionUtil.getUserid());
		
		List<SaleDTO> list = mapper.getList(form);;
		list.forEach(item -> {
			//면적종류
			item.setAreaKind(ForsaleUtil.getAreaKind(item));
			//평형
			item.setSupplyAreaPy(NumberUtil.m2ToPy(item.getSupplyArea()));
			item.setPrivateAreaPy(NumberUtil.m2ToPy(item.getPrivateArea()));
			item.setLandAreaPy(NumberUtil.m2ToPy(item.getLandArea()));
			item.setBuildingAreaPy(NumberUtil.m2ToPy(item.getBuildingArea()));
			//면적(평형)
			item.setSupplyAreaWithPy(NumberUtil.m2WithPy(item.getSupplyArea()));
			item.setPrivateAreaWithPy(NumberUtil.m2WithPy(item.getPrivateArea()));
			item.setLandAreaWithPy(NumberUtil.m2WithPy(item.getLandArea()));
			item.setBuildingAreaWithPy(NumberUtil.m2WithPy(item.getBuildingArea()));
			//편집URL
			item.setEditUrl(String.format("/register/03f03/%s", item.getRid()));
			//가격
			item.setPriceWon(NumberUtil.priceToStr(item));
			//관리비
			item.setMaintenanceFeeWon(("Y".equals(item.getMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));
			item.setShortMaintenanceFeeWon(("Y".equals(item.getShortMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", item.getMaintenanceFee()));	//단기 관리비
		});
		return list;
	}
	
	@Override
	public SaleDTO getSale(SaleDTO form) {
		SaleDTO data = mapper.getSale(form);
		if(data != null) {
			data.setPhone(NumberUtil.getFormatPhone(data.getPhone()));
			
			if( !StringUtils.isEmpty(data.getPicture01Path()) ) data.getPhotoList().add(data.getPicture01Path());
			if( !StringUtils.isEmpty(data.getPicture02Path()) ) data.getPhotoList().add(data.getPicture02Path());
			if( !StringUtils.isEmpty(data.getPicture03Path()) ) data.getPhotoList().add(data.getPicture03Path());
			if( !StringUtils.isEmpty(data.getPicture04Path()) ) data.getPhotoList().add(data.getPicture04Path());
			if( !StringUtils.isEmpty(data.getPicture05Path()) ) data.getPhotoList().add(data.getPicture05Path());
			if( !StringUtils.isEmpty(data.getPicture06Path()) ) data.getPhotoList().add(data.getPicture06Path());
			if( !StringUtils.isEmpty(data.getPicture07Path()) ) data.getPhotoList().add(data.getPicture07Path());
			if( !StringUtils.isEmpty(data.getPicture08Path()) ) data.getPhotoList().add(data.getPicture08Path());
			
			//면적종류
			String areaKind = ForsaleUtil.getAreaKind(data);
			data.setAreaKind(areaKind);
			
			//평형
			data.setSupplyAreaPy(NumberUtil.m2ToPy(data.getSupplyArea()));
			data.setPrivateAreaPy(NumberUtil.m2ToPy(data.getPrivateArea()));
			data.setLandAreaPy(NumberUtil.m2ToPy(data.getLandArea()));
			data.setBuildingAreaPy(NumberUtil.m2ToPy(data.getBuildingArea()));
			//면적(평형)
			data.setSupplyAreaWithPy(NumberUtil.m2WithPy(data.getSupplyArea()));
			data.setPrivateAreaWithPy(NumberUtil.m2WithPy(data.getPrivateArea()));
			data.setLandAreaWithPy(NumberUtil.m2WithPy(data.getLandArea()));
			data.setBuildingAreaWithPy(NumberUtil.m2WithPy(data.getBuildingArea()));
			//편집URL
			data.setEditUrl(String.format("/register/03f03/%s", data.getRid()));
			//가격
			data.setPriceWon(NumberUtil.priceToStr(data));
			//관리비
			data.setMaintenanceFeeWon(("Y".equals(data.getMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", data.getMaintenanceFee()));
			data.setShortMaintenanceFeeWon(("Y".equals(data.getShortMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", data.getMaintenanceFee()));	//단기 관리비
			/*
            <dd>원룸 | 면적 86㎡(23평) | 2층 / 총 10층 | 남동향 | 공용주차</dd>
            <dd>엘리베이터 있음 | 준공 2020년 | 입주협의</dd>
            <dd>반려동물 가능 | 단기 월 55만원 (2개월 이상)</dd>
            */
			String info1 = String.format("%s | 면적 %s | %s | %s | 주차 %s"
														, data.getSaleDetailTypeNm()
														, ("B".equals(areaKind))? NumberUtil.m2WithPy(data.getLandArea()) : NumberUtil.m2WithPy(data.getSupplyArea())	//대지이면 대지면적
														, NumberUtil.floorWithTotal(data.getSaleFloor(), data.getTotalFloor())
														, (data.getDirectionTypeNm()==null)? "-" : data.getDirectionTypeNm()
														, (data.getParkingTypeNm()==null)? "-" : data.getParkingTypeNm());
			String info2 = String.format("엘리베이터 %s | 준공 %s년 | 입주 %s"
					, data.getElevatorTypeNm()
					, data.getCompletionYear()
					, data.getMoveinTypeNm());
			String info3 = String.format("반려동물 %s %s"
					, data.getAnimalTypeNm()
					, ("B".equals(data.getTradeType()))? String.format("| 단기 월 %s만원 (%s개월 이상)" 
												, (data.getShortPrice()==null)? "-" : String.valueOf(data.getShortPrice())
												, (StringUtils.isEmpty(data.getShortMinMonth()))? "-" : data.getShortMinMonth()) : ""	//단기정보는 월세인 경우만.
					, data.getMoveinTypeNm());
			data.setInfo1(info1);
			data.setInfo2(info2);
			data.setInfo3(info3);
			
			data.setOwnerYn(data.getCreateId()!=null && data.getCreateId().equals(SessionUtil.getUserid())? "Y" : "N");	//매물등록자인지 여부.
		}
		return data;
	}
	
	@Override
	public SaleDTO getSaleEditing(SaleDTO form) {
		SaleDTO data = mapper.getSaleEditing(form);
		if(data != null) {
			if( !StringUtils.isEmpty(data.getPicture01Path()) ) data.getPhotoList().add(data.getPicture01Path());
			if( !StringUtils.isEmpty(data.getPicture02Path()) ) data.getPhotoList().add(data.getPicture02Path());
			if( !StringUtils.isEmpty(data.getPicture03Path()) ) data.getPhotoList().add(data.getPicture03Path());
			if( !StringUtils.isEmpty(data.getPicture04Path()) ) data.getPhotoList().add(data.getPicture04Path());
			if( !StringUtils.isEmpty(data.getPicture05Path()) ) data.getPhotoList().add(data.getPicture05Path());
			if( !StringUtils.isEmpty(data.getPicture06Path()) ) data.getPhotoList().add(data.getPicture06Path());
			if( !StringUtils.isEmpty(data.getPicture07Path()) ) data.getPhotoList().add(data.getPicture07Path());
			if( !StringUtils.isEmpty(data.getPicture08Path()) ) data.getPhotoList().add(data.getPicture08Path());
			
			
			//면적종류
			String areaKind = ForsaleUtil.getAreaKind(data);
			data.setAreaKind(areaKind);
			//평형
			data.setSupplyAreaPy(NumberUtil.m2ToPy(data.getSupplyArea()));
			data.setPrivateAreaPy(NumberUtil.m2ToPy(data.getPrivateArea()));
			data.setLandAreaPy(NumberUtil.m2ToPy(data.getLandArea()));
			data.setBuildingAreaPy(NumberUtil.m2ToPy(data.getBuildingArea()));
			//편집URL
			data.setEditUrl(String.format("/register/03f03/%s", data.getRid()));
			//가격
			data.setPriceWon(NumberUtil.priceToStr(data));
			//관리비
			data.setMaintenanceFeeWon(("Y".equals(data.getMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", data.getMaintenanceFee()));
			data.setShortMaintenanceFeeWon(("Y".equals(data.getShortMaintenanceYn()))? "관리비 없음" : String.format("관리비 %,d만원", data.getMaintenanceFee()));	//단기 관리비
			
			data.setOwnerYn(data.getCreateId()!=null && data.getCreateId().equals(SessionUtil.getUserid())? "Y" : "N");	//매물등록자인지 여부.
		}
		return data;
	}
	
	@Override
	public ResultDTO saveSale02(SaleDTO form) {

		int cnt = mapper.updateSale02Editing(form);
		if(cnt==0) {
			mapper.insertSaleEditing(form);
		}
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO saveSale03(SaleDTO form) {

		int cnt = mapper.updateSale03Editing(form);
		
		return (cnt==0)? ResultDTO.of(-1, "잘못된 매물 정보입니다.") : ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO saveSale04(SaleDTO form) {

		int cnt = mapper.updateSale04Editing(form);
		
		return (cnt==0)? ResultDTO.of(-1, "잘못된 매물 정보입니다.") : ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO saveSale05(SaleDTO form) {

		//고객 주소 조회(먼저 전체 주소(addr1 + addr2)로 조회해보고 데이터가 없으면 addr1로만 조회).
		DaumAddressDTO destination = DaumAddressUtil.getAddress(DAUM_RESTAPI_KEY, String.format("%s %s", form.getAddr1(), form.getAddr2()));
		if(destination.getCode() == 99) {
			return ResultDTO.of(destination.getCode(), destination.getMessage(), destination);
		}
		else if(destination.getCode() == -1) {	//addr1 + addr2로 검색했을때 데이터가 없는 경우.
			destination = DaumAddressUtil.getAddress(DAUM_RESTAPI_KEY, form.getAddr1());
		}
		
		log.info("Customer...{}", ObjectMapperUtil.toString(destination));
		if(destination.getCode() != 0) {
			return ResultDTO.of(destination.getCode(), destination.getMessage(), destination);
		}
		else {
			Address address = destination.getDocuments().get(0);
			RoadAddress road = address.getRoadAddress();
			JibunAddress jibun = address.getJibunAddress();
			
			form.setJibunAddr(jibun.getAddressName());
			form.setBcode(jibun.getBcode());
			form.setHcode(jibun.getHcode());
			form.setHdong(jibun.getHdongName());

			form.setSido(road.getSidoName());
			form.setSigungu(road.getSigunguName());
			form.setDong(road.getDongName());
			
			//좌표
			form.setPosX(address.getX());
			form.setPosY(address.getY());
		}
		//destination.getDocuments().get(0)
		
		MultipartFile file = null;
		
		try {
			String uploadPath = String.format("%s/forsale/%s", UPLOAD_BASE_PATH, DateUtil.getYm());
			String fileSavePath = String.format("%s%s", UPLOAD_BASE_FOLDER, uploadPath);
			log.info("■ uploadPath={}", uploadPath);
			log.info("■ fileSavePath={}", fileSavePath);
			
			//이미지파일.
			file = form.getPicture01File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture01Path(attachPath);
			}
			//이미지파일.
			file = form.getPicture02File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture02Path(attachPath);
			}
			//이미지파일.
			file = form.getPicture03File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture03Path(attachPath);
			}
			//이미지파일-4.
			file = form.getPicture04File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture04Path(attachPath);
			}
			//이미지파일-5.
			file = form.getPicture05File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture05Path(attachPath);
			}
			//이미지파일.
			file = form.getPicture06File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture06Path(attachPath);
			}
			//이미지파일.
			file = form.getPicture07File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture07Path(attachPath);
			}
			//이미지파일.
			file = form.getPicture08File();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setPicture08Path(attachPath);
			}
		}
		catch(Exception e) {
			log.error("[ERROR]\n{}", e);
			return ResultDTO.of(-1, "파일 저장시 오류가 발생하였습니다.");
		}

		int cnt = mapper.updateSale05Editing(form);
		
		return (cnt==0)? ResultDTO.of(-1, "잘못된 매물 정보입니다.") : ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO updateFinalStatus(SaleDTO form) {
		//forsale_editing 테이블의 데이터를 읽어서 forsale에 반영.
		int cnt = mapper.updateSale(form);
		if(cnt==0) {
			mapper.insertSale(form);
		}
		return ResultDTO.of(0, "등록되었습니다.", (cnt==0)?"n":"m");
	}
	
	@Override
	public ResultDTO deleteSale(SaleDTO form) {
		mapper.deleteSaleEditing(form);		//임시데이터 삭제.
		mapper.deleteSale(form);			//원데이터 삭제.
		return ResultDTO.of(0, "삭제되었습니다.");
	}
	
	@Override
	public ResultDTO toggleStatus(SaleDTO form) {
		mapper.updateStatus(form);			//상태 업데이트.
		return ResultDTO.of(0, ("00".equals(form.getStatus())? "노출 처리되었습니다." : "비노출 처리되었습니다."));
	}
	
	@Override
	public List<UserDTO> getAgentList(SaleDTO form) {
		mapper.updateViewCount(form);	//조회수 증가.
		return mapper.getAgentList(form);
	}
	
	@Override
	public ResultDTO checkOffer(SaleDTO form) {
		//매물.
		SaleDTO forsale = mapper.getSale(form);
		if(forsale != null) {
			forsale.setPhone(NumberUtil.getFormatPhone(forsale.getPhone()));
			//임대관리자는 원룸/투룸 종류만 알선하기 가능.
			if("RA".equals(SessionUtil.getUserType()) && !"r".equals(forsale.getSaleType())) {
	       		return ResultDTO.of(5, "임대관리자 회원의 경우 원룸/투룸만 알선하기 가능 합니다.", forsale);
			}
		}
		
		//소유자
		UserDTO owner = userMapper.getUser( UserDTO.of(forsale.getCreateId()) );
		owner.setPhone(NumberUtil.getFormatPhone(owner.getPhone()));
		owner.setPasswd(null);
		//사용자
		UserDTO user = userMapper.getUser( UserDTO.of(form.getSessionId()) );
		//포인트 적립/차감율.
       	PointRateDTO rate = pointMapper.getPointRate().stream().filter(item -> item.getSaleType().equals(forsale.getSaleType())).findFirst().orElse(null);
       	if(rate==null) rate = new PointRateDTO();
       	log.info("RATE...{}", ObjectMapperUtil.toString(rate));
/*
    	private Double subRateA = 0.0;	// 포인트차감-매매
    	private Double subRateB = 0.0;	// 포인트차감-월세
    	private Double subRateC = 0.0;	// 포인트차감-전세
    	private Double addRateR = 0.0;	// 포인트적립-알써니
    	private Double addRateO = 0.0;	// 포인트적립-소유자
    	private Double addRateM = 0.0;	// 포인트적립-관리자
*/
       	
       	PointDTO pointDTO = pointMapper.getPointByRid(form);
       	if(pointDTO != null) {
           	owner.setPhone(NumberUtil.getFormatPhone(owner.getPhone()));
           	owner.setManagerPhone(NumberUtil.getFormatPhone(owner.getManagerPhone()));
       		forsale.setOwnerInfo(owner);	//매물 등록자 정보 설정.
       		return ResultDTO.of(10, "이미 열람한 매물입니다.", forsale);
       	}
       	
       	long price = forsale.getPrice();
       	long point = 0;
       	double pointRate = 0.0;
       	if("A".equals(forsale.getTradeType())) {
       		pointRate = rate.getSubRateA();
       	}
       	else if("B".equals(forsale.getTradeType())) {
       		pointRate = rate.getSubRateB();
       		price = forsale.getPrice() + (forsale.getMonthlyPrice() * 100);	//월세인 경우...금액 = 보증금 + (월세 * 100)
       	}
       	else if("C".equals(forsale.getTradeType())) {
       		pointRate = rate.getSubRateC();
       	}

       	//필요 포인트.(금액은 만단위이므로 10000을 곱해줌)
   		point = Math.round((price * 10000) * (pointRate / 100.0));
   		//가용 포인트.
		long useAvailPoint = (user==null)? 0 : user.getUseAvailPoint();
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rid", form.getRid());
		data.put("price", price);
		data.put("point", point);
		data.put("rate", pointRate);
		data.put("useAvailPoint", useAvailPoint);
		data.put("brokerageType", forsale.getBrokerageType());
       	
       	log.info("price={}, point={}, availPoint={}", price, point, useAvailPoint);
		
       	if("C".equals(forsale.getBrokerageType())) {
			return ResultDTO.of(-1, "해당 매물은 단독중개 매물입니다.\n열람이 불가능 합니다.", data);
       	}
       	else if(point > useAvailPoint) {
			return ResultDTO.of(-1, String.format("열람을 위한 포인트가 부족합니다.\n포인트 메뉴에서 충전하세요.\n\n필요Point: %,10d", point), data);
		}
		else {
			return ResultDTO.of(0, "정상", data);
		}
	}
	
	@Override
	@Transactional
	public ResultDTO doOffer(SaleDTO form) {
		//매물.
		SaleDTO forsale = mapper.getSale(form);
		if(forsale != null) {
			forsale.setPhone(NumberUtil.getFormatPhone(forsale.getPhone()));
		}
		
		//소유자
		UserDTO owner = userMapper.getUser( UserDTO.of(forsale.getCreateId()) );
		owner.setPhone(NumberUtil.getFormatPhone(owner.getPhone()));
		owner.setPasswd(null);
		//사용자
		UserDTO user = userMapper.getUser( UserDTO.of(form.getSessionId()) );
		//포인트 차감율.
       	PointRateDTO rate = pointMapper.getPointRate().stream().filter(item -> item.getSaleType().equals(forsale.getSaleType())).findFirst().orElse(null);
       	if(rate==null) rate = new PointRateDTO();
       	log.info("RATE...{}", ObjectMapperUtil.toString(rate));
/*       	
    	private Double subRateA = 0.0;	// 포인트차감-매매
    	private Double subRateB = 0.0;	// 포인트차감-월세
    	private Double subRateC = 0.0;	// 포인트차감-전세
    	private Double addRateR = 0.0;	// 포인트적립-알써니
    	private Double addRateO = 0.0;	// 포인트적립-소유자
    	private Double addRateM = 0.0;	// 포인트적립-관리자
*/
       	long price = forsale.getPrice();
       	long point = 0;
       	double pointRate = 0.0;
       	if("A".equals(forsale.getTradeType())) {		//매매
       		pointRate = rate.getSubRateA();
       	}
       	else if("B".equals(forsale.getTradeType())) {	//월세
       		pointRate = rate.getSubRateB();
       		price = forsale.getPrice() + (forsale.getMonthlyPrice() * 100);	//월세인 경우...금액 = 보증금 + (월세 * 100)
       	}
       	else if("C".equals(forsale.getTradeType())) {	//전체
       		pointRate = rate.getSubRateC();
       	}

       	//필요 포인트.(금액은 만단위이므로 10000을 곱해줌)
   		point = Math.round((price * 10000) * (pointRate / 100.0));
   		//가용 포인트.
		long useAvailPoint = (user==null)? 0 : user.getUseAvailPoint();
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("rid", form.getRid());
		data.put("price", price);
		data.put("point", point);
		data.put("rate", pointRate);
		data.put("useAvailPoint", useAvailPoint);
		
		if(point > useAvailPoint) {
			return ResultDTO.of(-1, String.format("포인트가 부족합니다.\n\n필요Point: %,10d\n보유Point: %,10d", point, useAvailPoint), data);
		}
       	
       	log.info("price={}, point={}, availPoint={}", price, point, useAvailPoint);
       	
       	user.getRecommandId();
       	
       	long addPointO = Math.round(point * rate.getAddRateO() / 100.0);
       	long addPointM = 0;
       	log.info("recommand={}, point={}, availPoint={}", price, point, useAvailPoint);
      	if("Y".equals(owner.getRecommandExistYn())) {
       		addPointM = Math.round(point * rate.getAddRateM() / 100.0);	//지점매니저
       	}
       	long addPointR = point - (addPointO + addPointM);	//RSunny 포인트는 차감 포인트에서 소유자와 지점매니저의 적립포인트를 뺀 나머지임.
/*
 		form.setPoint(point);
		form.setInoutType("O");		//포인트사용
		form.setInoutDetail("OI");	//아이템구매
		form.setStatus("00");		//처리완료
		form.setUserid(SessionUtil.getUserid());
		form.setSessionId(SessionUtil.getUserid());
       	
 */
       	String grpPid = NumberUtil.getUniqueKey();

       	log.info("매물열람: 사용(열람)자ID={}	, 차감포인트={}", user.getUserid(), point);
       	log.info("매물열람: 소유자ID={}    		, 적립포인트={}", forsale.getCreateId(), addPointO);
       	log.info("매물열람: 지점관리자ID={}		, 적립포인트={}", owner.getRecommandId(), addPointM);
       	log.info("매물열람: 알써니ID={}    		, 적립포인트={}", RSUNNY_ID, addPointR);
       	
       	PointDTO pform = null;
       	pform = PointDTO.of(user.getUserid(), "O", "OS", "60", point, forsale.getRid(), grpPid, SessionUtil.getUserid());	//OS=차감, 60=차감대기.
       	pointMapper.insertPoint(pform);
		
       	if(addPointO > 0) {	//소유자 적립
           	pform = PointDTO.of(forsale.getCreateId(), "I", "IS", "20", addPointO, forsale.getRid(), grpPid, SessionUtil.getUserid());	//IS=적립, 20=적립대기.
           	pointMapper.insertPoint(pform);
       	}
		
       	if(addPointM > 0) {	//지점관리자 적립
           	pform = PointDTO.of(owner.getRecommandId(), "I", "IS", "20", addPointM, forsale.getRid(), grpPid, SessionUtil.getUserid());	//IS=적립, 20=적립대기.
           	pointMapper.insertPoint(pform);
       	}
		
       	if(addPointR > 0) {	//알써니 적립
           	pform = PointDTO.of(RSUNNY_ID, "I", "IS", "20", addPointR, forsale.getRid(), grpPid, SessionUtil.getUserid());	//IS=적립, 20=적립대기.
           	pointMapper.insertPoint(pform);
       	}

		mapper.updateOpenCount(form);	//열람수 증가.
		
		//사용자 정보 새로 가져옴.
		user = userMapper.getUser( UserDTO.of(form.getSessionId()) );
		forsale.setSessionUserAvailPoint(user.getUseAvailPoint());

       	user.setPasswd("");
       	owner.setPhone(NumberUtil.getFormatPhone(owner.getPhone()));
       	owner.setManagerPhone(NumberUtil.getFormatPhone(owner.getManagerPhone()));
   		forsale.setOwnerInfo(owner);	//매물 등록자 정보 설정.
   		
		return ResultDTO.of(0, "정상", forsale);
	}

	
	
	@Override
	public List<SaleDTO> getAddrSido(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getAddrSido(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getAddrSigungu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getAddrSigungu(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getAddrEmd(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getAddrEmd(form);
		
		return rtlist;
	}
	
	
	@Override
	public List<SaleDTO> getRealTradingsList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsList(form);
		
		for(int i=0; i<rtlist.size(); i++) {
			rtlist.get(i).setDealAmount(rtlist.get(i).getDealAmount() != null ? NumberUtil.priceToStr(rtlist.get(i).getDealAmount()) : "0");
			rtlist.get(i).setDeposit(rtlist.get(i).getDeposit() != null ? NumberUtil.priceToStr(rtlist.get(i).getDeposit()) : "0");
			rtlist.get(i).setMonthlyRent(rtlist.get(i).getMonthlyRent() != null ? NumberUtil.priceToStr(rtlist.get(i).getMonthlyRent()) : "0");
		}
		
		rtlist.forEach(item -> {
//			//면적종류
//			item.setAreaKind(ForsaleUtil.getAreaKind(item));
//			//평형
//			item.setSupplyAreaPy(NumberUtil.m2ToPy(item.getSupplyArea()));
//			item.setPrivateAreaPy(NumberUtil.m2ToPy(item.getPrivateArea()));
//			item.setLandAreaPy(NumberUtil.m2ToPy(item.getLandArea()));
//			item.setBuildingAreaPy(NumberUtil.m2ToPy(item.getBuildingArea()));
//			//면적(평형)
//			item.setSupplyAreaWithPy(NumberUtil.m2WithPy(item.getSupplyArea()));
//			item.setPrivateAreaWithPy(NumberUtil.m2WithPy(item.getPrivateArea()));
//			item.setLandAreaWithPy(NumberUtil.m2WithPy(item.getLandArea()));
//			item.setBuildingAreaWithPy(NumberUtil.m2WithPy(item.getBuildingArea()));
//
//			//편집URL
//			item.setEditUrl(String.format("/register/03f03/%s", item.getRid()));
			//가격(NumberUtil.priceToStr(item));
			System.out.println("item");
			System.out.println(item);
			//item.setDealAmount(NumberUtil.priceToStr(item));			
		});
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsAllList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsAllList(form);
		
		return rtlist;
	}
	
	@Override
	@Transactional
	public ResultDTO updateRealTradings(SaleDTO form) {
	
		mapper.updateRealTradings(form);
		
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public SaleDTO getRealTradingsListFromPnu(SaleDTO form) {
	
		SaleDTO rtlist = mapper.getRealTradingsListFromPnu(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsAptListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsAptListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsApt2ListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsApt2ListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsOfficetelListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsOfficetelListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsOfficetel2ListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsOfficetel2ListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsPresaleListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsPresaleListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsMultihouseListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsMultihouseListFromPnu(form);
		
		return rtlist;
	}
	@Override
	public List<SaleDTO> getRealTradingsMultihouse2ListFromPnu(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsMultihouse2ListFromPnu(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromApt1(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromApt1(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromApt2(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromApt2(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromOfficetel1(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromOfficetel1(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromOfficetel2(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromOfficetel2(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromMultihouse1(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromMultihouse1(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsListFromMultihouse2(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsListFromMultihouse2(form);
		
		return rtlist;
	}
	
	
	
	
	
	
	
	
	@Override
	public List<SaleDTO> getRealTradingsSeoulList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsSeoulList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsBusanList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsBusanList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsIncheonList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsIncheonList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsDaejeonList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsDaejeonList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsDaeguList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsDaeguList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsGwangjuList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsGwangjuList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsUlsanList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsUlsanList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsSejongList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsSejongList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsJejuList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsJejuList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsGyeonggiList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsGyeonggiList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsGangwonList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsGangwonList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsChungcheongbukdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsChungcheongbukdoList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsChungcheongnamdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsChungcheongnamdoList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsJeollabukdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsJeollabukdoList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsJeollanamdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsJeollanamdoList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsGyeongsangbukdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsGyeongsangbukdoList(form);
		
		return rtlist;
	}
	
	@Override
	public List<SaleDTO> getRealTradingsGyeongsangnamdoList(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getRealTradingsGyeongsangnamdoList(form);
		
		return rtlist;
	}
	
	
	
	@Override
	public ResultDTO insertTest(SaleDTO form) {
		//forsale_editing 테이블의 데이터를 읽어서 forsale에 반영.
		int cnt = 1;
		
		mapper.insertTest(form);
		
		return ResultDTO.of(0, "등록되었습니다.", (cnt==0)?"n":"m");
	}
	
	
	
	@Override
	public List<SaleDTO> getCollectCoords(SaleDTO form) {
	
		List<SaleDTO> rtlist = mapper.getCollectCoords(form);
		
		return rtlist;
	}
}
