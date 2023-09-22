package com.regroup.rsunny.common.service;

import java.util.List;

import com.regroup.rsunny.common.model.CodeDTO;

public interface CommonCodeService {

	List<CodeDTO> getList(String grp);

	List<CodeDTO> getList(CodeDTO form);
	
	CodeDTO getCode(CodeDTO form);

	List<CodeDTO> getUserList();

	List<CodeDTO> getCustCompanyList();

	List<CodeDTO> getCustCompanyList(CodeDTO form);

	List<CodeDTO> getCustManagerList(CodeDTO form);

}
