package com.es.sample.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountSaveReq {

	@ApiModelProperty(value="이름", dataType="String", position=20)
	private String name;

	@ApiModelProperty(value="이메일", dataType="String", position=30)
	private String email;
	
}
