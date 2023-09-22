package com.regroup.rsunny.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.common.model.CustCompanyDTO;
import com.regroup.rsunny.common.model.CustManagerDTO;

@Mapper
public interface CustomerMapper {

	//업체(고객사)
	List<CustCompanyDTO> getCompanyList(CustCompanyDTO form);

	int insertCompany(CustCompanyDTO form);

	int updateCompany(CustCompanyDTO form);

	//업체(고객사) 담당자
	List<CustManagerDTO> getManagerList(CustManagerDTO form);

	int insertManager(CustManagerDTO form);

	int updateManager(CustManagerDTO form);

}
