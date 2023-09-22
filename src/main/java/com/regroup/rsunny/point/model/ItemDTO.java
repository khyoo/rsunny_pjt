package com.regroup.rsunny.point.model;

import java.time.LocalDateTime;

import com.regroup.rsunny.common.model.PagingDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDTO extends PagingDTO {

	private Long iid;					// 식별번호
	private String userid;				// 사용자ID
	private String usernm;				// 사용자명
	private String phone;				// 사용자연락처
	private String email;				// 사용자이메일
	private String userType;			// 사용자유형
	private String userTypeNm;			// 사용자유형
	private String itemType;			// 아이템유형
	private String inoutType;			// 입출금 구분(IN/OUT)
	private String inoutTypeNm;			// 입출금 구분(IN/OUT)
	private String inoutDetail;			// 입출금 상세
	private Long itemCount;				// 수량
	private Long unitPrice;				// 단가
	private Long itemPrice;				// 금액
	private String saleType;			// 매물유형
	private String status;				// 상태
	private String rid;					// 적용매물ID
	private Long pid;					// 사용포인트ID
	private LocalDateTime startDtm;		// 적용시작일시
	private LocalDateTime endDtm;		// 적용종료일시
	private String remark;				// 비고
	private LocalDateTime createDtm;	// 생성일시
	private String createId;			// 생성자ID
	private LocalDateTime updateDtm;	// 수정일시
	private String updateId;			// 수정자ID
	

	private long useAvailItem;			//사용가능 아이템1 수
	private long useCount;				//아이템1 사용수
	private long applyCount;			//아이템2 적용수

}
