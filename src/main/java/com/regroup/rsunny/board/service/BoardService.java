package com.regroup.rsunny.board.service;

import java.util.List;

import com.regroup.rsunny.board.model.BoardDTO;
import com.regroup.rsunny.common.model.ResultDTO;

public interface BoardService {

	List<BoardDTO> getList(BoardDTO form);
	
	BoardDTO getBoard(BoardDTO form);
	
	ResultDTO saveBoard(BoardDTO form);
	
	ResultDTO updateAnswer(BoardDTO form);
	
	ResultDTO updateStatus(BoardDTO form);
	
	ResultDTO updateViewCount(BoardDTO form);
	
	ResultDTO deleteBoard(BoardDTO form);
	
	ResultDTO updateDisplayYn(BoardDTO form);
   
}
