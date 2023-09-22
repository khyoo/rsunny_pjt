package com.regroup.rsunny.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PagingDTO {
	private String sessionId;
	
	private String userid;
	
	private String sortType;
	
	private String schWord;
	
	private String action;
	
    private String seqTxt;
	
    private String pageType;
	
    private String crudType;
	
	private long sessionUserAvailPoint;					// 사용자 사용 가능 포인트.

    private int seq = 0;
	
    private int page = 1;

    private int rows = 10;

    private int frRow = 0;

    private int toRow = 0;

    private int totalCount = 0;

    private int pageCount = 0;

    private long rnk = 0;

    private boolean search = false;	//최초 화면 진입인지 체크용.
	
    private String pagingHtml;

	public int getFrRow() {
		return (page - 1) * rows + 1;
	}

	public int getToRow() {
		return page * rows;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getPageCount() {
		return (int) Math.ceil(1.0 * totalCount / rows);
	}
    
}
