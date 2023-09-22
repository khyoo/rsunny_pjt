package com.regroup.rsunny.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.regroup.rsunny.common.mapper.CommonCodeMapper;
import com.regroup.rsunny.common.model.CodeDTO;
import com.regroup.rsunny.common.service.CommonCodeService;

@Service
public class CommonCodeServiceImpl implements CommonCodeService {
	@Autowired
    private CommonCodeMapper mapper;

	@Override
    public List<CodeDTO> getList(String grp) {
		return mapper.getList(CodeDTO.of(grp));
	}

	@Override
    public List<CodeDTO> getList(CodeDTO form) {
		return mapper.getList(form);
	}

	@Override
    public CodeDTO getCode(CodeDTO form) {
		return mapper.getCode(form);
	}

	@Override
    public List<CodeDTO> getUserList() {
		return mapper.getUserList();
	}

	@Override
    public List<CodeDTO> getCustCompanyList() {
		return mapper.getCustCompanyList();
	}

	@Override
    public List<CodeDTO> getCustCompanyList(CodeDTO form) {
		return mapper.getCustCompanyList(form);
	}

	@Override
    public List<CodeDTO> getCustManagerList(CodeDTO form) {
		return mapper.getCustManagerList(form);
	}

}
