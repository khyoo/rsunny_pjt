package com.regroup.rsunny.forsale.model.address;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaumAddressDTO {
    
    private List<Address> documents = new ArrayList<Address>();
    private Meta meta;
    
    private int code;
    private String message;

    public static DaumAddressDTO of(int code, String message) {
    	DaumAddressDTO data = new DaumAddressDTO();
    	data.setCode(code);
    	data.setMessage(message);
    	return data;
    }
}

