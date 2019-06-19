package com.es.sample.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Account {

	@ApiModelProperty(value="고유번호", dataType="String")
	private String id;

	@ApiModelProperty(value="이름", dataType="String")
	private String name;

	@ApiModelProperty(value="이메일", dataType="String")
	private String email;
	
	@ApiModelProperty(value="가입일", dataType="LocalDateTime")
	private LocalDateTime regDt;
	
}
