package com.regroup.rsunny.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeDTO {

	String grp;
	String cd;
	String nm;
	String uppGrp;
	String uppCd;
	String useYn;

	public static CodeDTO of(String grp) {
		CodeDTO item = new CodeDTO();
		item.setGrp(grp);
		return item;
	}

	public static CodeDTO of(String grp, String cd) {
		CodeDTO item = new CodeDTO();
		item.setGrp(grp);
		item.setCd(cd);
		return item;
	}

	public static CodeDTO of(String cd, String nm, int i) {	//i는 메소드 중복을 피하기 위한 dummy.
		CodeDTO item = new CodeDTO();
		item.setCd(cd);
		item.setNm(nm);
		return item;
	}

	public static CodeDTO of(String grp, String uppGrp, String uppCd) {
		CodeDTO item = new CodeDTO();
		item.setGrp(grp);
		item.setUppGrp(uppGrp);
		item.setUppCd(uppCd);
		return item;
	}

}
