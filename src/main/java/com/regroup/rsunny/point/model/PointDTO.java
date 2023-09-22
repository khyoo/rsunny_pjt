package com.regroup.rsunny.point.model;

import java.time.LocalDateTime;

import com.regroup.rsunny.common.model.PagingDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PointDTO extends PagingDTO {

	private Long pid;					// 식별번호
	private String userid;				// 사용자ID
	private String usernm;				// 사용자명
	private String phone;				// 사용자연락처
	private String email;				// 사용자이메일
	private String userType;			// 사용자유형
	private String userTypeNm;			// 사용자유형
	private String inoutType;			// 입출금 구분(IN/OUT)
	private String inoutTypeNm;			// 입출금 구분(IN/OUT)
	private String pointSign;			// +/-구분
	private String inoutDetail;			// 입출금 상세
	private String inoutDetailNm;		// 입출금 상세
	private String inoutDetailNm2;		// 입출금 상세
	private String status;				// 상태
	private String statusNm;			// 상태
	private String inoutStatusNm;		// 입출금(상태)
	private String inoutDetailStatusNm;		// 입출금상세(상태)
	private Long useAvailPoint;			// 사용가능금액
	private Long withdrawalAvailPoint;	// 사용가능금액
	private Long point;					// 금액
	private String bankNm;				// 은행
	private String accountNo;			// 계좌번호
	private String accountOwner;		// 계좌소유자
	private String rid;					// 관련매물ID
	private String grpPid;				// 관련포인트그룹(취소시 일괄취소)
	private String remark;				// 비고
	private String detailRemark;		// 상세내용
	private LocalDateTime createDtm;	// 생성일시
	private String createId;			// 생성자ID
	private LocalDateTime updateDtm;	// 수정일시
	private String updateId;			// 수정자ID

	private String frontType;			// 프론트에서 조회하는 포인트 타입
	
	public static PointDTO of(Long pid) {
		PointDTO dto = new PointDTO();
		dto.setPid(pid);
		
		return dto;
	}
	
	public static PointDTO of(String rid, String sessionId) {
		PointDTO dto = new PointDTO();
		dto.setRid(rid);
		dto.setSessionId(sessionId);
		
		return dto;
	}
	
	public static PointDTO of(String userid, String inoutType, String inoutDetail, String status, long point, String rid, String grpPid, String sessionId) {
		PointDTO dto = new PointDTO();
		dto.setUserid(userid);
		dto.setInoutType(inoutType);
		dto.setInoutDetail(inoutDetail);
		dto.setStatus(status);
		dto.setPoint(point);
		dto.setRid(rid);
		dto.setGrpPid(grpPid);
		dto.setSessionId(sessionId);
		
		return dto;
	}

}
