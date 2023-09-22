package com.regroup.rsunny.utils;

import com.regroup.rsunny.common.model.SaleDTO;

public class ForsaleUtil {

    public static String getAreaKind(SaleDTO data) {
    	//l=토지/건물, h1=(주택/빌라)단독/전원, h3=(주택/빌라)민박/펜션
    	if("l".equals(data.getSaleType()) || "h1".equals(data.getSaleDetailType()) || "h3".equals(data.getSaleDetailType())) {
    		return "B";		//대지면적/건축면적
    	}
    	else {
    		return "A";		//공급면적/전용면적
    	}
    }

}
