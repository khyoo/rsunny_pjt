package com.regroup.rsunny.forsale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.forsale.mapper.ReportMapper;
import com.regroup.rsunny.forsale.model.ReportDTO;
import com.regroup.rsunny.forsale.service.ReportService;
import com.regroup.rsunny.point.mapper.PointMapper;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.ObjectMapperUtil;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
    private ReportMapper mapper;
	
	@Autowired
    private PointMapper pointMapper;

	@Override
	public List<ReportDTO> getList(ReportDTO form) {
		List<ReportDTO> list = mapper.getList(form);
		for(ReportDTO item : list) {
			item.setPriceWon(NumberUtil.priceToStr(item));
		}
		return list;
	}
	
	@Override
	public ReportDTO getReport(ReportDTO form) {
		return mapper.getReport(form);
	}
	
	@Override
	public ResultDTO saveReport(ReportDTO form) {
		int cnt = mapper.updateReport(form);
		if(cnt==0) {
			mapper.insertReport(form);
		}
		
		return ResultDTO.of(0, "신고접수가 완료 되었습니다.\n알써니 담당자 검토 후 처리 예정입니다.");
	}
	
	@Override
	public ResultDTO refundPoint(ReportDTO form) {
		PointDTO point = pointMapper.getPoint(PointDTO.of(form.getPid()));
		if( point != null && !"60".equals(point.getStatus()) ) {
			return ResultDTO.of(-1, String.format("포인트 반환 불가한 상태입니다(status=%s)", point.getStatus()));
		}
		
		List<PointDTO> pointList = pointMapper.getPointListByGrp(form);
		if(pointList.size() > 0) form.setStatusMemo(ObjectMapperUtil.toString(pointList));
		
		int cnt = pointMapper.deletePointByGrp(form);
		if(cnt==0) {
			return ResultDTO.of(-2, "이미 반환하였거나 반환할 포인트가 없습니다.");
		}
		else {
			mapper.updateStatusRefund(form);
			return ResultDTO.of(0, "포인트 반환이 완료되었습니다.");
		}
	}

}
