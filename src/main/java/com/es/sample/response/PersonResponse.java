package com.es.sample.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PersonResponse {

	@ApiModelProperty(value="이름", dataType="String")
	private String name;

	@ApiModelProperty(value="이메일", dataType="String")
	private String email;
	
	@ApiModelProperty(value="국가(지역)", dataType="String")
	private String nation;
	
	@ApiModelProperty(value="나이", dataType="String")
	private int age;
	
	@ApiModelProperty(value="성별", dataType="String")
	private String gender;
	
	@ApiModelProperty(value="취미", dataType="String")
	private String hobby;
	
	@ApiModelProperty(value="가입일", dataType="String")
	private String joindate;
}
