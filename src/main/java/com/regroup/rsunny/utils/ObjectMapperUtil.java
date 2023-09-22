package com.regroup.rsunny.utils;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ObjectMapperUtil {

    public static String toString(Object data) {
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(data);
    	}
    	catch(Exception e) {
    		return null;
    	}
    }

}
