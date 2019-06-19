package com.es.sample.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.sample.elasticUtil.ElasticApi;
import com.es.sample.entity.Account;
import com.es.sample.request.AccountReq;
import com.es.sample.request.AccountSaveReq;
import com.es.sample.util.ObjectUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@Api(tags = "ES 유저관리 샘플", value = "ES 유저관리 샘플", description = "ES 유저관리 샘플")
@RequestMapping("/user")
public class EsRestController {

	@Autowired
	ElasticApi elasticApi;
	
	private final String ACCOUNT_URL = "bae/bae";
	
	@ApiOperation(value = "유저리스트 조회하기")
	@GetMapping(value="/findByUser/{id}")
	public ResponseEntity<Account> findByUser(@PathVariable String id){
		
		String url = ACCOUNT_URL + "/" + id;
		Map<String, Object> result = elasticApi.callElasticApi("GET", url, null, null);
		log.info(result.get("resultCode").toString());
		log.info("---------------------------------");
		log.info(result.get("resultBody").toString());
		
		Account account = (Account) ObjectUtil.convertMapToObject(result, new Account());	
		
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	@ApiOperation(value = "유저 저장하기")
	@PostMapping(value="/insertUser")
	public ResponseEntity<Account> insertUser(@RequestBody AccountSaveReq accountSaveReq){

		String url = ACCOUNT_URL;
		Account account = Account.builder()
				.name(accountSaveReq.getName())
				.email(accountSaveReq.getEmail())
				.regDt(LocalDateTime.now())
				.build();
		
		Map<String, Object> result = elasticApi.callElasticApi("POST", url, account, null);
		
		log.info(result.get("resultCode").toString());
		log.info("---------------------------------");
		log.info(result.get("resultBody").toString());
		
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "유저 수정하기")
	@PutMapping(value="/updateUser")
	public ResponseEntity<Account> updateUser(@RequestBody AccountReq accountReq){

		String id = accountReq.getId(); //"zCqmbmsBQ6D5XaOU2EKg";
		String url = ACCOUNT_URL + "/" + id;
		Account account = Account.builder()
				.name(accountReq.getName())
				.email(accountReq.getEmail())
				.regDt(LocalDateTime.now())
				.build();
		
		Map<String, Object> result = elasticApi.callElasticApi("PUT", url, account, null);
		
		log.info(result.get("resultCode").toString());
		log.info("---------------------------------");
		log.info(result.get("resultBody").toString());
		
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "유저 삭제하기")
	@DeleteMapping(value="/deleteUser/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id){

		String url = ACCOUNT_URL + "/" + id;
		Map<String, Object> result = elasticApi.callElasticApi("DELETE", url, null, null);
		log.info(result.get("resultCode").toString());
		log.info("---------------------------------");
		log.info(result.get("resultBody").toString());
		
		return new ResponseEntity<>("TEST SUCCESS", HttpStatus.OK);
	}
	
	
	
	
	
}
