package com.regroup.rsunny.board.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.regroup.rsunny.board.mapper.BoardMapper;
import com.regroup.rsunny.board.model.BoardDTO;
import com.regroup.rsunny.board.service.BoardService;
import com.regroup.rsunny.common.model.ResultDTO;
import com.regroup.rsunny.utils.DateUtil;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
    private BoardMapper mapper;
	
	@Value("${rsunny.file.upload-path}")
	private String UPLOAD_BASE_PATH;
	
	@Value("${rsunny.file.upload-folder}")
	private String UPLOAD_BASE_FOLDER;

	@Override
	public List<BoardDTO> getList(BoardDTO form) {
		return mapper.getList(form);
	}
	
	@Override
	public BoardDTO getBoard(BoardDTO form) {
		return mapper.getBoard(form);
	}
	
	@Override
	public ResultDTO saveBoard(BoardDTO form) {
		
		String uploadPath = String.format("%s/board/%s", UPLOAD_BASE_PATH, DateUtil.getYm());
		String fileSavePath = String.format("%s%s", UPLOAD_BASE_FOLDER, uploadPath);
		
		MultipartFile file = null;
		
		try {
			//이미지파일.
			file = form.getImageFile();
			if(file != null && file.getSize() > 0) {
				String attachName = file.getOriginalFilename();
				String attachExt = FilenameUtils.getExtension(attachName);
				String fileName = String.format("%s.%s", UUID.randomUUID().toString(), attachExt);
				String attachPath = String.format("%s/%s", uploadPath, fileName);
				File destFile = new File(String.format("%s/%s", fileSavePath, fileName));
				destFile.getParentFile().mkdirs();
				file.transferTo(destFile);
				form.setImagePath(attachPath);
			}
		}
		catch(Exception e) {
			log.error("[ERROR]\n{}", e);
			return ResultDTO.of(-1, "파일 저장시 오류가 발생하였습니다.");
		}

		form.setSessionId(SessionUtil.getUserid());
		
		int cnt = mapper.updateBoard(form);
		if(cnt==0) {
			mapper.insertBoard(form);
		}
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO updateAnswer(BoardDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		mapper.updateAnswer(form);
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO updateStatus(BoardDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		mapper.updateStatus(form);
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO updateViewCount(BoardDTO form) {
		mapper.updateViewCount(form);
		return ResultDTO.of(0, "저장되었습니다.");
	}
	
	@Override
	public ResultDTO deleteBoard(BoardDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		mapper.deleteBoard(form);
		return ResultDTO.of(0, "삭제되었습니다.");
	}
	
	@Override
	public ResultDTO updateDisplayYn(BoardDTO form) {
		form.setSessionId(SessionUtil.getUserid());
		
		mapper.updateDisplayYn(form);
		return ResultDTO.of(0, "저장되었습니다.");
	}

}
