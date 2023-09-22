package com.regroup.rsunny.forsale.service;

import java.util.List;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.forsale.model.ReportDTO;

public interface ReportService {

	List<ReportDTO> getList(ReportDTO form);
	
	ReportDTO getReport(ReportDTO form);
	
	ResultDTO saveReport(ReportDTO form);
	
	ResultDTO refundPoint(ReportDTO form);

}
