package com.regroup.rsunny.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.regroup.rsunny.board.model.BoardDTO;

@Mapper
public interface BoardMapper {

	List<BoardDTO> getList(BoardDTO form);
	
	BoardDTO getBoard(BoardDTO form);
	
	int insertBoard(BoardDTO form);
	
	int updateBoard(BoardDTO form);
	
	int updateAnswer(BoardDTO form);
	
	int updateStatus(BoardDTO form);
	
	int updateViewCount(BoardDTO form);
	
	int deleteBoard(BoardDTO form);
	
	int updateDisplayYn(BoardDTO form);
	
}
