package com.regroup.rsunny.forsale.model.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
	@JsonProperty("address")
    private JibunAddress jibunAddress;
	
    @JsonProperty("address_name")
    private String addressName;
    
    @JsonProperty("address_type")
    private String addressType;
	
    @JsonProperty("road_address")
    private RoadAddress roadAddress;
    
    private String x;
    private String y;

}

