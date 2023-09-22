package com.regroup.rsunny.system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.regroup.rsunny.common.model.PagingDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends PagingDTO {
	private String userid;				// 사용자ID
	private String usernm;				// 사용자명
	private String passwd;				// 비밀번호
	private String userType;			// 사용자유형
	private String userTypeNm;			// 사용자유형
	private String authType;			// 권한유형
	private String authTypeNm;			// 권한유형
	private String phone;				// 연락처
	private String email;				// 이메일
	
	private String bankNm;				// 은행명
	private String accountNo;			// 계좌번호
	private String bankUpdateYn;		// 계좌정보 업데이트 여부
	
	private String bizNm;				// 공인중개사명
	private String ceoNm;				// 대표자명
	private String addr1;				// 기본주소
	private String addr2;				// 상세주소
	private String managerNm;			// 담당자명
	private String managerPhone;		// 담당자연락처
	
	private String bizPath;				// 사업자등록증경로
	private String licensePath;			// 자격증명경로
	private String openPath;			// 개설등록증경로
	private String deductiblePath;		// 공제증서경로
	private String outsidePath;			// 사무실외부경로
	private String insidePath;			// 사무실내부경로
	private String additionalPath;		// 추가파일경로
	
	private String recommandId;			// 추천인ID
	private String belongBranch;		// 소속지점
	private String lastLoginDtm;		// 최종로그인일시
	private String status;				// 상태
	private String statusNm;			// 상태
	private Integer stopDays;			// 정지기간
	@JsonDeserialize(using = LocalDateDeserializer.class) @JsonSerialize(using = LocalDateSerializer.class) @JsonFormat(pattern = "yyyy-MM-dd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate stopFrDate;		// 정지시작일
	@JsonDeserialize(using = LocalDateDeserializer.class) @JsonSerialize(using = LocalDateSerializer.class) @JsonFormat(pattern = "yyyy-MM-dd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate stopToDate;		// 정지종료일
	
	private int salesOpenCnt;			//매물열람 건수
	private int adItemCnt;				//광고아이템 건수
	private int agentCnt;				//연결중개사 건수
	private long point;					//포인트
	private long useAvailPoint;			//사용가능포인트
	private long withdrawalAvailPoint;	//출금가능포인트
	private int salesCnt;				//등록매물 건수
	private int openCnt;				//등록열람 건수
	private long useAvailItem;			//사용가능 아이템1 수
	
	private LocalDateTime createDtm;			// 생성일시
	private String createId;			// 생성자ID
	private LocalDateTime updateDtm;			// 수정일시
	private String updateId;			// 수정자ID
	
	private String privateProvisionYn;	// 개인정보 제3자 제공여부.
	private String pp;	// 개인정보 제3자 제공여부.
	
	private MultipartFile bizFile;
	private MultipartFile licenseFile;
	private MultipartFile openFile;
	private MultipartFile deductibleFile;
	private MultipartFile outsideFile;
	private MultipartFile insideFile;
	private MultipartFile additionalFile;
	
	private String jibunAddr;				//지번주소
	private String sido;					//광역시도
	private String sigungu;					//시도군
	private String dong;					//동(법정동)
	private String hdong;					//동(행정동)
	private String bcode;					//법정동코드
	private String hcode;					//행정동코드
	private String posX;					//위도(가로선)
	private String posY;					//경도(세로선)
	private String dongAddr;				//동주소

	private String recommandExistYn;			// 추천인존재여부
	
	private String userToken;				//사용자 아이디 토큰.
	private String emailSubject;			//비밀번호 찾기 메일 제목.
	private String senderEmail;				//보내는 사람 이메일
	
	private String isNew;						// 신규여부

	public static UserDTO of(String userid) {
		UserDTO user = new UserDTO();
		user.setUserid(userid);
		return user;
	}
}
