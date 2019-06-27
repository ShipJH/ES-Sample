package com.es.sample.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value="고객 저장 모델")
public class PersonSaveReq {
	
	@ApiModelProperty(value="이름", dataType="String" ,position=10)
	private String name;

	@ApiModelProperty(value="이메일", dataType="String" ,position=20)
	private String email;
	
	@ApiModelProperty(value="국가(지역)", dataType="String" ,position=30)
	private String nation;
	
	@ApiModelProperty(value="나이", dataType="String" ,position=40)
	private int age;
	
	@ApiModelProperty(value="성별", dataType="String" ,position=50)
	private String gender;
	
	@ApiModelProperty(value="취미", dataType="String" ,position=60)
	private String hobby;
	
}
