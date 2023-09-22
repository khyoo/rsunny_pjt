package com.regroup.rsunny.board.controller;

import java.util.Arrays;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regroup.rsunny.board.model.BoardDTO;
import com.regroup.rsunny.board.service.BoardService;
import com.regroup.rsunny.common.model.ResultDTO;

import groovyjarjarpicocli.CommandLine.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board")
@RestController
public class AdminBoardRestController { 

	@Autowired
	private BoardService service;

	/**
	 * 게시물 조회
	 * @param BoardDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@RequestMapping("/{btype}/view/{bid}")
	public BoardDTO view(@PathVariable String btype, @PathVariable String bid, BoardDTO form, Model model) throws Exception {
		log.info("...Board.save...btype={}...bid={}", form.getBtype(), form.getBid());
		return service.getBoard(form);
	}

	/**
	 * 게시물 저장
	 * @param BoardDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{btype}/rest/save")
	public ResultDTO save(@PathVariable String btype, BoardDTO form, Model model) throws Exception {
		log.info("...Board.save...btype={}...bid={}", form.getBtype(), form.getBid());
		return service.saveBoard(form);
	}

	/**
	 * 게시물 저장
	 * @param BoardDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{btype}/rest/answer")
	public ResultDTO answer(@PathVariable String btype, BoardDTO form, Model model) throws Exception {
		log.info("...Board.save...btype={}...bid={}", form.getBtype(), form.getBid());
		return service.updateAnswer(form);
	}

	/**
	 * 게시물 삭제
	 * @param BoardDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{btype}/rest/delete")
	public ResultDTO delete(@PathVariable String btype, BoardDTO form, Model model) throws Exception {
		log.info("...Board.save...btype={}...bid={}...selList={}", form.getBtype(), form.getSelList(), form.getSelList());
		//선택된 값이 없으면 bid값을 설정.
		if( StringUtils.isEmpty(form.getSelList()) ) form.setSelList(String.valueOf(form.getBid()));
		
		//log.info("...Board.save...btype={}...bid={}...selList={}", form.getBtype(), form.getSelList(), form.getSelList());
		form.setItemList(Arrays.asList(form.getSelList().split(",")));
		return service.deleteBoard(form);
	}

	/**
	 * 게시물 노출/비노출
	 * @param BoardDTO
	 * @return ResultDTO
	 * @throws Exception
	 */
	@PostMapping("/{btype}/rest/display")
	public ResultDTO display(@PathVariable String btype, BoardDTO form, Model model) throws Exception {
		log.info("...Board.save...btype={}...bid={}...selList={}", form.getBtype(), form.getSelList(), form.getSelList());
		//선택된 값이 없으면 bid값을 설정.
		if( StringUtils.isEmpty(form.getSelList()) ) form.setSelList(String.valueOf(form.getBid()));
		
		//log.info("...Board.save...btype={}...bid={}...selList={}", form.getBtype(), form.getSelList(), form.getSelList());
		form.setItemList(Arrays.asList(form.getSelList().split(",")));
		return service.updateDisplayYn(form);
	}

}
