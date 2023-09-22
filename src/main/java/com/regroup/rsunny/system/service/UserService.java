package com.regroup.rsunny.system.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.system.model.UserDTO;

public interface UserService {

    UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException;

    ResultDTO checkLogin(UserDTO form);
    
    List<UserDTO> getList(UserDTO form);
    
    UserDTO getUser(UserDTO form);

    ResultDTO saveUser(UserDTO form);

    ResultDTO updateAgentUser(UserDTO form);

    ResultDTO deleteUser(UserDTO form);

    ResultDTO approveUser(UserDTO form);

    ResultDTO stopUser(UserDTO form);

    ResultDTO changeUserType(UserDTO form);

	List<UserDTO> getBranchList();
    
    List<UserDTO> getLinkedAgentList(UserDTO form);
    
    ResultDTO findUser(UserDTO form);
    
    ResultDTO changePasswd(UserDTO form);

}
