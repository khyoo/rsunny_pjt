package com.regroup.rsunny.forsale.model.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JibunAddress {
	@JsonProperty("address_name")
    private String addressName;
	
	@JsonProperty("b_code")
    private String bcode;
	
	@JsonProperty("h_code")
    private String hcode;
	
	@JsonProperty("region_1depth_name")
    private String sidoName;
	
	@JsonProperty("region_2depth_name")
    private String sigunguName;
	
	@JsonProperty("region_3depth_name")
    private String dongName;
	
	@JsonProperty("region_3depth_h_name")
    private String hdongName;
	
	@JsonProperty("main_address_no")
    private String mainAddressNo;
	
	@JsonProperty("sub_address_no")
    private String subAddressNo;

    private String postCode;

}

