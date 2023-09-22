package com.regroup.rsunny.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.common.model.CodeDTO;

@Mapper
public interface CommonCodeMapper {

	List<CodeDTO> getList(CodeDTO form);

	CodeDTO getCode(CodeDTO form);

	List<CodeDTO> getUserList();

	List<CodeDTO> getCustCompanyList();

	List<CodeDTO> getCustCompanyList(CodeDTO form);

	List<CodeDTO> getCustManagerList(CodeDTO form);

}
