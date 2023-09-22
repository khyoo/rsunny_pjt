package com.trading.collector;

import org.springframework.beans.factory.annotation.Autowired;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.forsale.service.ForsaleService;

public class TradingCollector {

	@Autowired
	private ForsaleService service;
	
	public ResultDTO insert() {
		ResultDTO result = service.insertTest(null);
		
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println("dkfj");
		
		
		TradingCollector tc = new TradingCollector();
		
		tc.insert();
	}
}
