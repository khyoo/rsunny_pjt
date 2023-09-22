package com.regroup.rsunny.board.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.regroup.rsunny.common.model.PagingDTO;
import com.regroup.rsunny.utils.SessionUtil;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardDTO extends PagingDTO {

	private Long bid;				// 게시물ID
	private String btype;			// 게시물유형
	private String title;			// 제목
	private String contents;		// 내용
	private String contentsHtml;	// 내용
	private String answer;			// 답변
	private String linkUrl;			// 링크URL
	private String imagePath;		// 이미지경로
	private String name;			// 성명
	private String email;			// 이메일
	private String phone;			// 연락처
	private String status;			// 상태코드
	private String statusNm;		// 상태코드
	private String displayYn;		// 노출여부
	private Long viewCount;			// 조회수
	private LocalDateTime createDtm;		// 생성일시
	private String createId;		// 생성자ID
	private String createNm;		// 생성자명
	private LocalDateTime updateDtm;		// 수정일시
	private String updateId;		// 수정자ID
	
	private MultipartFile imageFile;	//이미지파일
	
	private String selList;
	private List<String> itemList;
	
	public static BoardDTO display(String btype, String displayYn) {
		BoardDTO dto = new BoardDTO();
		dto.setBtype(btype);
		dto.setDisplayYn(displayYn);
		
		return dto;
	}

}
