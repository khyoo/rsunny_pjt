package com.regroup.rsunny.forsale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.forsale.model.ReportDTO;

@Mapper
public interface ReportMapper {

	List<ReportDTO> getList(ReportDTO form);
	
	ReportDTO getReport(ReportDTO form);
	
	int insertReport(ReportDTO form);
	
	int updateReport(ReportDTO form);
	
	int updateStatusRefund(ReportDTO form);
	
}
