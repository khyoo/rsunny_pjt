package com.regroup.rsunny.forsale.model.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
	@JsonProperty("is_end")
    private boolean isEnd;
	
	@JsonProperty("pageable_count")
    private int pageableCount;
	
	@JsonProperty("total_count")
    private int totalCount;

}

