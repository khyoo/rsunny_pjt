package com.regroup.rsunny.common.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.regroup.rsunny.system.model.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleDTO extends PagingDTO {

	private String t;
	private String w;

	private Long id;						// 매물번호
	private String rid;						// 매물KEY
	private String saleNo;					// 매물번호
	private String brokerageType;			// 중개유형
	private String saleType;				// 매물유형
	private String saleTypeNm;				// 매물유형
	private String saleDetailType;			// 매물상세유형
	private String saleDetailTypeNm;		// 매물상세유형
	private String tradeType;				// 거래유형
	private String tradeTypeNm;				// 거래유형
	private Long price;						// 거래금액
	private String priceWon;				// 거래금액
	private Long maintenanceFee;			// 관리비
	private String maintenanceFeeWon;		// 관리비WON
	private String maintenanceYn;			// 관리비여부
	private Long monthlyPrice;				// 월세금액
	private Long shortPrice;				// 단기금액
	private String shortMaintenanceFeeWon;	// 단기 관리비WON
	private String shortMaintenanceYn;		// 단기 관리비여부
	private String shortMinMonth;			// 단기 최소기간(월)
	private String includeItems;			// 포함 항목
	private Double area;					// 공급면적
	private Double supplyArea;				// 공급면적
	private Integer supplyAreaPy;			// 공급면적
	private Double privateArea;				// 전용공간
	private Integer privateAreaPy;			// 전용공간
	private Double landArea;				// 대지면적
	private Integer landAreaPy;				// 대지면적
	private Double buildingArea;			// 건축면적
	private Integer buildingAreaPy;			// 건축면적
	private Integer totalFloor;				// 총 층수
	private Integer saleFloor;				// 매물 층수
	private String directionType;			// 방향
	private String directionTypeNm;			// 방향
	private String parkingType;				// 주차 유형
	private String parkingTypeNm;			// 주차 유형
	private String roomType;				// 방/거실 개수
	private String roomTypeNm;				// 방/거실 개수
	private String bathroomType;			// 욕실
	private String bathroomTypeNm;			// 욕실
	private String elevatorType;			// 엘레베이터
	private String elevatorTypeNm;			// 엘레베이터
	private String animalType;				// 반려동물
	private String animalTypeNm;			// 반려동물
	private String moveinType;				// 입주유형
	private String moveinTypeNm;			// 입주유형
	private String completionYm;			// 준공년월
	private String completionYear;			// 준공년월
	private String optionItems;				// 옵션 항목
	private String firstPicturePath;		// 첫번째 사진 경로
	private String picture01Path;			// 매물사진1
	private String picture02Path;			// 매물사진2
	private String picture03Path;			// 매물사진3
	private String picture04Path;			// 매물사진4
	private String picture05Path;			// 매물사진5
	private String picture06Path;			// 매물사진6
	private String picture07Path;			// 매물사진7
	private String picture08Path;			// 매물사진8
	private String forsaleIntro;			// 매물소개
	private String forsaleIntroDetail;		// 매물소개-상세
	private String hashtag;					// 해쉬태그
	private String addr1;					// 주소1
	private String addr2;					// 상세주소
	private String ownerNm;					// 소유자명
	private String phone;					// 연락처
	private String email;					// 이메일
	private String userEmail;				// 사용자정보의 이메일
	private String featuresIntro;			// 특장점
	private String privateProvisionYn;		// 개인정보 수집 동의여부
	private String status;					// 등록상태
	private String statusNm;				// 등록상태
	private int openCount;					// 열람수
	private LocalDateTime createDtm;		// 등록일시
	private String createId;				// 등록자ID
	private String saleUsernm;				// 등록자ID
	private LocalDateTime updateDtm;		// 수정일시
	private String updateId;				// 수정자ID
	
	private String jibunAddr;				//지번주소
	private String sido;					//광역시도
	private String sigungu;					//시도군
	private String emd;					//읍면동
	private String dong;					//동(법정동)
	private String hdong;					//동(행정동)
	private String bcode;					//법정동코드
	private String hcode;					//행정동코드
	private String posX;					//위도(가로선)
	private String posY;					//경도(세로선)
	private String dongAddr;				//동주소
	
	private String emdCond;					//읍면동 IN 조건
	private String sidoCode;				//시도코드
	
	private String jibun;
	private String jibun0;
	private String jibun1;
	private String jibun2;
	private String apartName;
	private String monthlyType;
	private String useArea;
	private String groundArea;
	private String dealYear;
	private String dealYearStart;
	private String dealYearEnd;
	private String dealMonth;
	private String dealDay;
	private String dealAmount;
	private String deposit;
	private String monthlyRent;
 	private String floor;
	private String buildYear;
	private String roadnmAddr;
	private String cancelDealDay;
	private String reqGBN;
	private String rdealerLawdnm;
	private String buildType;
	
	private String pnu;
	private String pnu0;	
	private String selSaleDetailType;
	private String selTradeType;
	
	private String posMinX;					//위도(가로선) - BoundBox MinX
	private String posMinY;					//경도(세로선) - BoundBox MinY
	private String posMaxX;					//위도(가로선) - BoundBox MaxX
	private String posMaxY;					//경도(세로선) - BoundBox MaxY

	private String editMode;				//편집모드(C=신규, U=수정)
	private String areaTypeNm;				//평형 정보를 하나의 문자열로 처리.
	private String editUrl;					//편집 URL

	private String linkUrl;					//상세보기 URL

	private int viewCount = 0;				//조회수.
	
	private MultipartFile picture01File;
	private MultipartFile picture02File;
	private MultipartFile picture03File;
	private MultipartFile picture04File;
	private MultipartFile picture05File;
	private MultipartFile picture06File;
	private MultipartFile picture07File;
	private MultipartFile picture08File;
	
	private String postL;
	private String postM;
	private String postS;
	
	private String info1;
	private String info2;
	private String info3;
	
	private String supplyAreaWithPy;				// 공급면적
	private String privateAreaWithPy;				// 전용면적
	private String landAreaWithPy;					// 대지면적
	private String buildingAreaWithPy;				// 빌딩면적

	private String areaKind;				//A=공급면적/전용면적, B=대지면적/건축면적
	
	private String ownerYn;
	
	private List<String> photoList = new ArrayList<String>();
	
	private List<UserDTO> agentList = new ArrayList<UserDTO>();

	private UserDTO ownerInfo;
	
}
