package com.regroup.rsunny.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.system.model.LogDTO;
import com.regroup.rsunny.system.model.UserDTO;

@Mapper
public interface UserMapper {

	List<UserDTO> getList(UserDTO form);
	
	UserDTO getUser(UserDTO form);
	
	UserDTO getUser(String form);
	
	int insertUser(UserDTO form);
	
	int updateUser(UserDTO form);
	
	int updateAgentUser(UserDTO form);
	
	int updateLastLogin(UserDTO form);
	
	int deleteUser(UserDTO form);
	
	int updateStatus(UserDTO form);
	
	int updateUserType(UserDTO form);

	List<UserDTO> getBranchList();

	List<UserDTO> getLinkedAgentList(UserDTO form);

	List<UserDTO> getListByEmail(UserDTO form);
	
	int updatePasswd(UserDTO form);
	
	int insertLogs(LogDTO form);

	List<LogDTO> getUserLogs(LogDTO form);

}
