package com.es.sample.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.es.sample.elasticUtil.ElasticTypeEnum;
import com.es.sample.entity.Account;
import com.es.sample.request.AccountReq;
import com.es.sample.request.AccountSaveReq;
import com.es.sample.response.AccountResponse;
import com.es.sample.util.MsgEum;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@Api(tags = "ES 유저관리 샘플", value = "ES 유저관리 샘플", description = "ES 유저관리 샘플")
@RequestMapping("/user")
public class EsRestController {

	private final ElasticApi elasticApi;
	@Autowired
	public EsRestController(ElasticApi elasticApi) {
		this.elasticApi = elasticApi;	
	}
	
	private final String ACCOUNT_URL = "bae/bae";
	private final String INDEX_URL = "bae/";
	
	
	@ApiOperation(value = "유저 조회하기")
	@GetMapping(value="/findByUser/{id}")
	public ResponseEntity<Map<String, Object>> findByUser(@PathVariable String id){
		
		String url = ACCOUNT_URL + "/" + id;
		Map<String, Object> result = elasticApi.callElasticApi("GET", url, null, null);
		
		String strJson = result.get("resultBody").toString();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = mapper.readValue(strJson, HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(map, HttpStatus.OK);
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
		
		
		System.out.println("@@@@");
		System.out.println("@@@@ result >> " );
		System.out.println("@@@@");
		
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
	
	
	
	@ApiOperation(value = "이메일 중복 체크")
	@GetMapping(value="/duplUserChk/{email}/", produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> DuplUserChk(@PathVariable String email) throws JSONException{
		
		String url = INDEX_URL + ElasticTypeEnum._SEARCH.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"query\": { ");
		sb.append(" 	\"term\": {");
		sb.append("			\"email.keyword\": { ");
		sb.append("				\"value\": \""+ email+"\"");
		sb.append("			}");
		sb.append("		}");
		sb.append("	}");
		sb.append("}");

		Map<String, Object> result = elasticApi.getSearch(url, sb.toString());
		
		String strJson = result.get("resultBody").toString();
		
		JSONObject jsonObj = new JSONObject(strJson);
		JSONObject hitJson = (JSONObject) jsonObj.get("hits");
		JSONObject totalJson = (JSONObject) hitJson.get("total");
		int val = (int) totalJson.get("value");
		
		String msg = val != 1 ? MsgEum.USE.getMsg() : MsgEum.DUPLICATION.getMsg() ;
		
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
	
	@ApiOperation(value = "모든 유저 조회하기")
	@GetMapping(value="/findAllUser")
	public ResponseEntity<List<AccountResponse>> findAllUser() throws JSONException{
		
		String url = INDEX_URL + ElasticTypeEnum._SEARCH.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { " );
		sb.append("     \"query\" : { " );
		sb.append("         \"match_all\" : {} " );
		sb.append("     } " );
		sb.append(" } " );
		
		Map<String, Object> result = elasticApi.getSearch(url, null);
		
		
		JSONObject jsonObj = new JSONObject(result.get("resultBody").toString());
		JSONArray jsonResultArray = jsonObj.getJSONObject("hits").getJSONArray("hits");
		
		List<AccountResponse> response = new ArrayList<>();
		JSONObject obj = null;
		JSONObject responseJsonObj = null;
		for(int i=0; i < jsonResultArray.length(); i++) {
			
			obj = (JSONObject) jsonResultArray.get(i);
			responseJsonObj = (JSONObject) obj.get("_source");
			
			response.add(AccountResponse.builder()
										.name(responseJsonObj.get("name").toString())
										.email(responseJsonObj.get("email").toString())
										.build());
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	@ApiOperation(value = "모든 필드에서 검색어 조회하기")
	@GetMapping(value="/findAllField/{param}")
	public ResponseEntity<List<AccountResponse>> findAllField(@PathVariable String param) throws JSONException{
		
		
		String url = INDEX_URL + ElasticTypeEnum._SEARCH.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { ");
		sb.append("   \"query\": { \"query_string\": { \"query\": \"*"+param+"*\" } } ");
		sb.append(" } ");
		
		Map<String, Object> result = elasticApi.getSearch(url, sb.toString());
		
		JSONObject jsonObj = new JSONObject(result.get("resultBody").toString());
		JSONArray jsonResultArray = jsonObj.getJSONObject("hits").getJSONArray("hits");
		
		List<AccountResponse> response = new ArrayList<>();
		JSONObject obj = null;
		JSONObject responseJsonObj = null;
		for(int i=0; i < jsonResultArray.length(); i++) {
			
			obj = (JSONObject) jsonResultArray.get(i);
			responseJsonObj = (JSONObject) obj.get("_source");
			
			response.add(AccountResponse.builder()
										.name(responseJsonObj.get("name").toString())
										.email(responseJsonObj.get("email").toString())
										.build());
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
}





