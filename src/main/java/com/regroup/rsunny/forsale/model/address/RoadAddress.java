package com.regroup.rsunny.forsale.model.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoadAddress {
	@JsonProperty("address_name")
    private String addressName;
	
	@JsonProperty("building_name")
    private String buildingName;
	
	@JsonProperty("main_building_no")
    private String mainBuildingNo;
	
	@JsonProperty("sub_building_no")
    private String subBuildingNo;
	
	@JsonProperty("region_1depth_name")
    private String sidoName;
	
	@JsonProperty("region_2depth_name")
    private String sigunguName;
	
	@JsonProperty("region_3depth_name")
    private String dongName;
	
	@JsonProperty("zone_no")
    private String zoneNo;

}

