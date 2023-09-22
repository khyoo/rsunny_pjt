package com.regroup.rsunny.system.model;

import com.regroup.rsunny.common.model.PagingDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDTO extends PagingDTO {
	private String userid;				// 사용자ID
	private String logType;				// 로그유형
	private String logData;				// 로그데이터

	public static LogDTO of(String userid, String logType, String logData) {
		LogDTO dto = new LogDTO();
		dto.setUserid(userid);
		dto.setLogType(logType);
		dto.setLogData(logData);
		
		return dto;
	}
}
