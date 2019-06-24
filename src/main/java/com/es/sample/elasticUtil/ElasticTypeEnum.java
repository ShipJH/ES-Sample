package com.es.sample.elasticUtil;

import lombok.Getter;
import lombok.Setter;

public enum ElasticTypeEnum {

	_SEARCH("_search"),
	_CAT("_cat"),
	;
	
	@Getter @Setter
	private String type;
	
	private ElasticTypeEnum(String type) {
		this.type = type;
	}
}
