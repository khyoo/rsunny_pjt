package com.regroup.rsunny.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.regroup.rsunny.point.service.PointService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class PointBatchController { 

	@Autowired
	private PointService pointService;

	/**
	 * 포인트 처리.
	 * @param 
	 * @return 
	 * @throws Exception
	 */
	@Scheduled(cron = "0 */10 * * * *")
	public void pointProcess() throws Exception {
		log.info("Point status batch start...");
		
		pointService.processPoint();
		
		log.info("Point status batch end.");
		
	}

}
