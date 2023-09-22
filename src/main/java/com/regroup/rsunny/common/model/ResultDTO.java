package com.regroup.rsunny.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResultDTO {
	private int code = 0;
	private String message;
	private Object data;
	
    public static ResultDTO of(int code, String message) {
    	ResultDTO dto = new ResultDTO();
    	dto.setCode(code);
    	dto.setMessage(message);
     	
        return dto;
    }	
	
    public static ResultDTO of(int code, String message, Object data) {
    	ResultDTO dto = new ResultDTO();
    	dto.setCode(code);
    	dto.setMessage(message);
    	dto.setData(data);
    	
        return dto;
    }	

}
