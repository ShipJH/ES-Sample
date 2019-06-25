package com.es.sample.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MsgEum {

	SUCCESS(0, "성공"),
	FAIL(-1, "실패"),
	DUPLICATION(-1, "중복되었습니다."),
	USE(0, "사용가능합니다.")
	;
	
	@Getter
	private int resultValue;
	
	@Getter
	private String msg;
	
	
	
}
