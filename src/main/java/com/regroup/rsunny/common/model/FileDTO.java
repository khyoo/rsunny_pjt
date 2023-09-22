package com.regroup.rsunny.common.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDTO extends PagingDTO {

    Long fid;
    Long bid;
	String ftype;
	String attachPath;
	String attachName;
	String attachExt;
	LocalDateTime regDate;
	String regId;

	public static FileDTO of(Long bid, String ftype) {
		FileDTO item = new FileDTO();
		item.setBid(bid);
		item.setFtype(ftype);
		return item;
	}

}
