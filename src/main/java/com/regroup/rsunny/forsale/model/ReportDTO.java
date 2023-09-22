package com.regroup.rsunny.forsale.model;

import java.time.LocalDateTime;

import com.regroup.rsunny.common.model.SaleDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportDTO extends SaleDTO {

	private Long cid;					// 식별ID
	private String rid;					// 매물ID
	private String agentId;				// 공인중개사ID
	private String agentNm;				// 공인중개사명
	private String reportType;			// 신고유형
	private String reportTypeNm;		// 신고유형
	private String etcMemo;				// 신고내용
	private String reportStatus;		// 상태
	private String reportstatusNm;		// 상태
	private Long pid;					// 알선하기 출금 포인트 식별번호
	private String grpPid;				// 알선하기 출금대기 & 입금대기  포인트 그룹
	private LocalDateTime createDtm;	// 생성일시
	private LocalDateTime reportDtm;	// 생성일시
	private String createId;			// 생성자ID
	private LocalDateTime updateDtm;	// 수정일시
	private String updateId;			// 수정자ID
	
	private String statusMemo;			//포인트 상태에 따른 시스템에서 남기는 메모.

	private String userType;			//사용자유형
	private String reporterId;			//신고자ID
	private String reporterNm;			//신고자명
	
	private String pageType;			//매물신고/중개사신고
}
