package com.regroup.rsunny.system.service.impl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.config.security.Role;
import com.regroup.rsunny.forsale.model.address.Address;
import com.regroup.rsunny.forsale.model.address.DaumAddressDTO;
import com.regroup.rsunny.forsale.model.address.JibunAddress;
import com.regroup.rsunny.forsale.model.address.RoadAddress;
import com.regroup.rsunny.point.mapper.PointMapper;
import com.regroup.rsunny.point.model.PointDTO;
import com.regroup.rsunny.system.mapper.UserMapper;
import com.regroup.rsunny.system.model.LogDTO;
import com.regroup.rsunny.system.model.UserDTO;
import com.regroup.rsunny.system.model.UserDetail;
import com.regroup.rsunny.system.service.UserService;
import com.regroup.rsunny.utils.AES256Util;
import com.regroup.rsunny.utils.DateUtil;
import com.regroup.rsunny.utils.DaumAddressUtil;
import com.regroup.rsunny.utils.MailUtil;
import com.regroup.rsunny.utils.NumberUtil;
import com.regroup.rsunny.utils.ObjectMapperUtil;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
    private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${rsunny.recommand.branch-point}")
	private long RECOMMAND_BRANCH_POINT;
	
	@Value("${rsunny.recommand.agent-point}")
	private long RECOMMAND_AGENT_POINT;
	
	@Value("${rsunny.daum.restapi-key}")
	private String DAUM_RESTAPI_KEY;
	
	@Value("${rsunny.file.upload-path}")
	private String UPLOAD_BASE_PATH;
	
	@Value("${rsunny.file.upload-folder}")
	private String UPLOAD_BASE_FOLDER;

	@Autowired
    private PointMapper pointMapper;
	
	@Override
    public ResultDTO checkLogin(UserDTO form) {
        UserDTO user = mapper.getUser(form);

        if(user == null || !passwordEncoder.matches(form.getPasswd(), user.getPasswd()) ) {
        	log.error("[ERROR-LOGIN] Not found user...userid={}", form.getUserid());
        	return ResultDTO.of(-1, "아이디 또는 비밀번호가 유효하지 않습니다.\n다시 확인해 주세요.");
        }
        else if(user != null && "95".equals(user.getStatus()) ) {
        	log.error("[ERROR-LOGIN] Stopped user...userid={}", form.getUserid());
        	return ResultDTO.of(-1, "관리자에 의해 일시 사용정지된 사용자입니다.");
        }
        else if(user != null && "99".equals(user.getStatus()) ) {
        	log.error("[ERROR-LOGIN] Deleted user...userid={}", form.getUserid());
        	return ResultDTO.of(-1, "아이디 또는 비밀번호가 유효하지 않습니다.\n다시 확인해 주세요.");
        }
        else if(user != null && "00".equals(user.getStatus()) ) {
        	log.error("[ERROR-LOGIN] Stopped user...userid={}", form.getUserid());
        	return ResultDTO.of(-1, "관리자 승인후 이용 가능합니다.");
        }
        else {
            //권한
            List<GrantedAuthority> authorities = new ArrayList<>();
            if("SU".equals(user.getUserType()) || "RM".equals(user.getUserType())) {	//슈퍼관리자 및 알써니관리자만...
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }
            else {
                authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
            }

            UserDetail authUser = new UserDetail(user.getUserid(), user.getPasswd(), authorities, user.getUsernm(), user.getUserType(), user.getPhone(), user.getEmail(), user.getPoint());
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, null, authorities);

            HttpServletRequest request = SessionUtil.getCurrentRequest();
            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
            
            //최종 로그인일시 업데이트.
            mapper.updateLastLogin(user);
            
            return ResultDTO.of(0, "로그인 성공");
        }
	}
	
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UserDTO user = mapper.getUser(userid);

        if(user == null) {
        	log.error("[ERROR-LOGIN] Not found user...userid={}", userid);
        	throw new UsernameNotFoundException("User not found");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));

        return new UserDetail(user.getUserid(), user.getPasswd(), authorities, user.getUsernm(), user.getUserType(), user.getPhone(), user.getEmail(), user.getPoint());
    }
    
    @Override
    public List<UserDTO> getList(UserDTO form) {
    	return mapper.getList(form);
    }
    
    @Override
    public UserDTO getUser(UserDTO form) {
    	return mapper.getUser(form);
    }
	
	@Override
    public ResultDTO saveUser(UserDTO form) {
		UserDTO user = mapper.getUser(form);
		
		String uploadPath = String.format("%s/user/%s", UPLOAD_BASE_PATH, DateUtil.getYm());
		String fileSavePath = String.format("%s%s", UPLOAD_BASE_FOLDER, uploadPath);
		
		if(user != null && ("C".equals(form.getCrudType()) || "Y".equals(form.getIsNew()) || "99".equals(user.getStatus()))) {
			return ResultDTO.of(-1, "사용할 수 없는 아이디입니다.");
		}
		
		//삭제된 사용자에 있는지 확인.
		List<LogDTO> logList = mapper.getUserLogs(LogDTO.of(form.getUserid(), "user-delete", null));
		if(logList.size() > 0) {
			return ResultDTO.of(-99, "사용할 수 없는 아이디입니다.");
		}

		if(user != null && StringUtils.isEmpty(form.getPasswd())) {
			form.setPasswd(user.getPasswd());
		}
		else {
			form.setPasswd(passwordEncoder.encode(form.getPasswd()));
		}
		
		//추천인 아이디가 있는 경우 유효한지 체크
		if( !StringUtils.isEmpty(form.getRecommandId()) ) {
			UserDTO manager = mapper.getUser(UserDTO.of(form.getRecommandId()));
			
			if(manager==null || !"BM".equals(manager.getUserType()) || !"90".equals(manager.getStatus())) {
				return ResultDTO.of(-2, "추천 매니저 아이디가 유효하지 않습니다.");
			}
		}
    	
		//사용자 유형.
    	if("a".equals(form.getUserType())) form.setUserType("AA");
    	else if("r".equals(form.getUserType())) form.setUserType("RA");
    	
    	
    	if("AA".equals(form.getUserType()) || "RA".equals(form.getUserType())) {
    		//고객 주소 조회(먼저 전체 주소(addr1 + addr2)로 조회해보고 데이터가 없으면 addr1로만 조회).
    		DaumAddressDTO destination = DaumAddressUtil.getAddress(DAUM_RESTAPI_KEY, String.format("%s %s", form.getAddr1(), form.getAddr2()));
    		if(destination.getCode() == 99) {
    			return ResultDTO.of(destination.getCode(), destination.getMessage(), destination);
    		}
    		else if(destination.getCode() == -1) {	//addr1 + addr2로 검색했을때 데이터가 없는 경우.
    			destination = DaumAddressUtil.getAddress(DAUM_RESTAPI_KEY, form.getAddr1());
    		}
    		
    		log.info("Customer...{}", ObjectMapperUtil.toString(destination));
    		if(destination.getCode() != 0) {
    			return ResultDTO.of(destination.getCode(), destination.getMessage(), destination);
    		}
    		else {
    			Address address = destination.getDocuments().get(0);
    			RoadAddress road = address.getRoadAddress();
    			JibunAddress jibun = address.getJibunAddress();
    			
    			form.setJibunAddr(jibun.getAddressName());
    			form.setBcode(jibun.getBcode());
    			form.setHcode(jibun.getHcode());
    			form.setHdong(jibun.getHdongName());

    			form.setSido(road.getSidoName());
    			form.setSigungu(road.getSigunguName());
    			form.setDong(road.getDongName());
    			
    			//좌표
    			form.setPosX(address.getX());
    			form.setPosY(address.getY());
    		}
    		//destination.getDocuments().get(0)
    	}
		
		MultipartFile file = null;
		
		try {
			//사업자등록증.
			file = form.getBizFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setBizPath(attachPath);
			}
			//자격증사본.
			file = form.getLicenseFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setLicensePath(attachPath);
			}
			//개설등록증.
			file = form.getOpenFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setOpenPath(attachPath);
			}
			//공제증서.
			file = form.getDeductibleFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setDeductiblePath(attachPath);
			}
			//사무실 외부.
			file = form.getOutsideFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setOutsidePath(attachPath);
			}
			//사무실 내부.
			file = form.getInsideFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setInsidePath(attachPath);
			}
			//기타.
			file = form.getAdditionalFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setAdditionalPath(attachPath);
			}
		}
		catch(Exception e) {
			log.error("[ERROR]\n{}", e);
			return ResultDTO.of(-3, "파일 저장시 오류가 발생하였습니다.");
		}
		
		
		int cnt = mapper.updateUser(form);
		if(cnt==0) {
			mapper.insertUser(form);
		}
		
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
    public ResultDTO updateAgentUser(UserDTO form) {
		UserDTO user = mapper.getUser(form);

		if(user != null && StringUtils.isEmpty(form.getPasswd())) {
			form.setPasswd(user.getPasswd());
		}
		else {
			form.setPasswd(passwordEncoder.encode(form.getPasswd()));
		}
		
		mapper.updateAgentUser(form);
		
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
    public ResultDTO deleteUser(UserDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		UserDTO user = mapper.getUser(form);
		if(user != null) {
			try {
				String data = ObjectMapperUtil.toString(user);
				mapper.insertLogs(LogDTO.of(form.getUserid(), "user-delete", AES256Util.encrypt(data)));
			}
			catch(Exception e) {
				log.error("[ERROR] {}", e);
				return ResultDTO.of(-1, "삭제 처리시 오류가 발생하였습니다.");
			}
		}
		/*
		form.setStopFrDate(null);
		form.setStopToDate(null);
		form.setStatus("99");
		mapper.updateStatus(form);
		*/
		mapper.deleteUser(form);
		
		return ResultDTO.of(0, "삭제 처리되었습니다.");
		
	}
	
	@Override
    public ResultDTO approveUser(UserDTO form) {
		
		form.setSessionId(SessionUtil.getUserid());
		
		UserDTO user = mapper.getUser(form);
		log.info("공인/임대관리사 승인 시작...user-id={}, recommand-id={}", user.getUserid(), user.getRecommandId());
		

		//추천인 아이디가 있는 경우 추천 포인트 부여
		if( !StringUtils.isEmpty(user.getRecommandId()) ) {
			UserDTO manager = mapper.getUser(UserDTO.of(user.getRecommandId()));
			log.info("...지점관리자 확인...{}", manager);
			
			//매니저 정보가 존재하는 경우.
			if(manager != null) {
				//포인트 적립/차감율.
				log.info("...부여할 포인트...지점=[{}]{}, 공인/임대=[{}]{}", manager.getUserid(), RECOMMAND_BRANCH_POINT, user.getUserid(), RECOMMAND_AGENT_POINT);

		       	//포인트 그룹 식별키.
		       	String grpPid = NumberUtil.getUniqueKey();

		       	PointDTO pform = null;
		       	//지점매니저 적립.
		       	if(RECOMMAND_BRANCH_POINT > 0) {
		           	pform = PointDTO.of(manager.getUserid(), "I", "IR", "00", RECOMMAND_BRANCH_POINT, null, grpPid, SessionUtil.getUserid());	//IR=추천, 00=처리완료.
			       	pointMapper.insertPoint(pform);
		       	}
		       	//공인/임대관리자 적립.
		       	if(RECOMMAND_AGENT_POINT > 0) {
		           	pform = PointDTO.of(user.getUserid(), "I", "IR", "00", RECOMMAND_AGENT_POINT, null, grpPid, SessionUtil.getUserid());	//IR=추천, 00=처리완료.
			       	pointMapper.insertPoint(pform);
		       	}
			}
			
		}
		
		form.setStatus("90");
		mapper.updateStatus(form);
		
		return ResultDTO.of(0, "승인되었습니다.");
	}
	
	@Override
    public ResultDTO stopUser(UserDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		int stopDays = (form.getStopDays()==null)? 7 : form.getStopDays();
		
		if(form.getStopDays()==0) {
			form.setStopFrDate(null);
			form.setStopToDate(null);
			form.setStatus("90");
			mapper.updateStatus(form);
			
			return ResultDTO.of(0, "정지 해제 처리되었습니다.");
		}
		if(form.getStopDays() == -99) {
			UserDTO user = mapper.getUser(form);
			if(user != null) {
				try {
					String data = ObjectMapperUtil.toString(user);
					mapper.insertLogs(LogDTO.of(form.getUserid(), "user-delete", AES256Util.encrypt(data)));
				}
				catch(Exception e) {
					log.error("[ERROR] {}", e);
					return ResultDTO.of(-1, "삭제 처리시 오류가 발생하였습니다.");
				}
			}
			/*
			form.setStopFrDate(null);
			form.setStopToDate(null);
			form.setStatus("99");
			mapper.updateStatus(form);
			*/
			mapper.deleteUser(form);
			
			return ResultDTO.of(0, "삭제 처리되었습니다.");
		}
		else {
			form.setStopFrDate(LocalDate.now());
			form.setStopToDate(LocalDate.now().plusDays(stopDays));
			form.setStatus("95");
			mapper.updateStatus(form);
			
			return ResultDTO.of(0, "정지 처리되었습니다.");
		}
	}
	
	@Override
    public ResultDTO changeUserType(UserDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		mapper.updateUserType(form);
		
		return ResultDTO.of(0, "지점매니저로 전환되었습니다.");
	}

    @Override
    public List<UserDTO> getBranchList() {
    	return mapper.getBranchList();
    }

    @Override
    public List<UserDTO> getLinkedAgentList(UserDTO form) {
    	return mapper.getLinkedAgentList(form);
    }
	
	@Override
    public ResultDTO findUser(UserDTO form) {
		List<UserDTO> list = mapper.getListByEmail(form);
		
		String contents = MailUtil.getContent("find-user");
		int succCnt = 0;
		for(UserDTO user : list) {
			try {
				String encUserid = AES256Util.encrypt(user.getUserid());
				String linkUrl = String.format("%s/06ma01/%s", MailUtil.getBaseLinkURL(), encUserid);
				String userContents = contents.replace("#USERID#", user.getUserid()).replace("#LINK_URL#", linkUrl);
				log.debug(userContents);
				
				ResultDTO result = MailUtil.sendMail(user.getEmail(), form.getEmailSubject(), userContents, form.getSenderEmail());
				if(result.getCode()==0) succCnt++;
				
			} catch(Exception e) {
				log.error("[ERROR] {}", e);
			}
		}
		
		return (list.size() > 0 && succCnt > 0) ? ResultDTO.of(0, "이메일을 전송하였습니다.") : ResultDTO.of(-1, "등록되지 않은 이메일입니다.");
	}
	
	@Override
    public ResultDTO changePasswd(UserDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		form.setPasswd(passwordEncoder.encode(form.getPasswd()));
		int cnt = mapper.updatePasswd(form);
		
		return (cnt==0)? ResultDTO.of(1, "잘못된 사용자 아이디입니다.") : ResultDTO.of(0, "변경되었습니다.");
	}
   
}
