package com.es.sample.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AccountResponse {

	@ApiModelProperty(value="이름", dataType="String", position=20)
	private String name;

	@ApiModelProperty(value="이메일", dataType="String", position=30)
	private String email;
}
