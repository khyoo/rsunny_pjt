package com.regroup.rsunny.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.regroup.rsunny.common.mapper.CustomerMapper;
import com.regroup.rsunny.common.model.CustCompanyDTO;
import com.regroup.rsunny.common.model.CustManagerDTO;
import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.common.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
    private CustomerMapper mapper;

	//업체(고객사)
	@Override
    public List<CustCompanyDTO> getCompanyList(CustCompanyDTO form) {
		return mapper.getCompanyList(form);
	}

	@Override
    public ResultDTO saveCompany(CustCompanyDTO form) {
		if(form.getCustCompId()==null) {
			List<CustCompanyDTO> list = mapper.getCompanyList(form);
			if(list.size() > 0) return ResultDTO.of(-1, "이미 등록된 업체입니다.");
		}
		int iAffected = mapper.updateCompany(form);
		if(iAffected==0) {
			iAffected =  mapper.insertCompany(form);
		}
		return ResultDTO.of(0, "저장되었습니다.");
	}

	//업체(고객사) 담당자
	@Override
    public List<CustManagerDTO> getManagerList(CustManagerDTO form) {
		return mapper.getManagerList(form);
	}

	@Override
    public ResultDTO saveManager(CustManagerDTO form) {
		if(form.getCustMngrId()==null) {
			List<CustManagerDTO> list = mapper.getManagerList(form);
			if(list.size() > 0) return ResultDTO.of(-1, "이미 등록된 담당자입니다.");
		}
		int iAffected = mapper.updateManager(form);
		if(iAffected==0) {
			iAffected =  mapper.insertManager(form);
		}
		return ResultDTO.of(0, "저장되었습니다.");
	}

}
