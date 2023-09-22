package com.regroup.rsunny.common.service;

import java.util.List;

import com.regroup.rsunny.common.model.CustCompanyDTO;
import com.regroup.rsunny.common.model.CustManagerDTO;
import com.regroup.rsunny.common.model.ResultDTO;

public interface CustomerService {

	//업체(고객사)
	List<CustCompanyDTO> getCompanyList(CustCompanyDTO form);

	ResultDTO saveCompany(CustCompanyDTO form);

	//업체(고객사) 담당자
	List<CustManagerDTO> getManagerList(CustManagerDTO form);

	ResultDTO saveManager(CustManagerDTO form);
   
}
