package com.es.sample.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.sample.elasticUtil.ElasticApi;
import com.es.sample.elasticUtil.ElasticTypeEnum;
import com.es.sample.entity.Person;
import com.es.sample.request.PersonSaveReq;
import com.es.sample.response.PersonResponse;
import com.es.sample.util.FormatUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@Api(tags = "고객 정보", value = "고객 정보", description = "고객 정보")
@RequestMapping("/person")
public class EsRestController2 {
	
	private final ElasticApi elasticApi;
	@Autowired
	public EsRestController2(ElasticApi elasticApi) {
		this.elasticApi = elasticApi;	
	}
	
	private final String PERSON_URL = "person/person";
	private final String INDEX_URL = "person/";
	
	
	@ApiOperation(value = "사람 저장하기")
	@PostMapping(value="/insertPerson",
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
	        produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> insertPerson(PersonSaveReq personSaveReq){

		String url = PERSON_URL;
		
		Person person = Person.builder()
						.name(personSaveReq.getName())
						.email(personSaveReq.getEmail())
						.nation(personSaveReq.getNation())
						.age(personSaveReq.getAge())
						.gender(personSaveReq.getGender())
						.hobby(personSaveReq.getHobby())
						.joindate(FormatUtil.isoTypeNowDate())
						.build();
		
		Map<String, Object> result = elasticApi.callElasticApi("POST", url, person, null);
		
		System.out.println("---------------------------------");
		System.out.println("resultCode > " + result.get("resultCode").toString());
		System.out.println("resultBody > " + result.get("resultBody").toString());
		System.out.println("---------------------------------");
		
		return new ResponseEntity<>(person, HttpStatus.OK);
	}
	
	@ApiOperation(value = "모든 필드에서 검색어 조회하기")
	@GetMapping(value="/findAllField/{param}")
	public ResponseEntity<List<PersonResponse>> findAllField(@PathVariable String param) throws JSONException{
		
		
		String url = INDEX_URL + ElasticTypeEnum._SEARCH.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { ");
		sb.append(" \"size\": 100,  " );
		sb.append("   \"query\": { \"query_string\": { \"query\": \"*"+param+"*\" } } ");
		sb.append(" } ");
		
		Map<String, Object> result = elasticApi.getSearch(url, sb.toString());
		
		JSONObject jsonObj = new JSONObject(result.get("resultBody").toString());
		JSONArray jsonResultArray = jsonObj.getJSONObject("hits").getJSONArray("hits");
		
		List<PersonResponse> response = new ArrayList<>();
		JSONObject obj = null;
		JSONObject responseJsonObj = null;
		for(int i=0; i < jsonResultArray.length(); i++) {
			
			obj = (JSONObject) jsonResultArray.get(i);
			responseJsonObj = (JSONObject) obj.get("_source");
			
			response.add(PersonResponse.builder()
										.name(responseJsonObj.get("name").toString())
										.email(responseJsonObj.get("email").toString())
										.nation(responseJsonObj.get("nation").toString())
										.age(responseJsonObj.getInt("age"))
										.gender(responseJsonObj.get("gender").toString())
										.hobby(responseJsonObj.get("hobby").toString())
										.build());
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}	
	
	
	@ApiOperation(value = "모든 사람 조회하기")
	@GetMapping(value="/findAllUser")
	public ResponseEntity<List<PersonResponse>> findAllUser() throws JSONException{
		
		String url = INDEX_URL + ElasticTypeEnum._SEARCH.getType();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" { " );
		sb.append(" \"size\": 100,  " );
		sb.append("  \"query\": { " );
		sb.append("  \"match_all\": {} " );
		sb.append("  } " );
		sb.append(" } " );
		
		
		Map<String, Object> result = elasticApi.getSearch(url, sb.toString());
		
		
		JSONObject jsonObj = new JSONObject(result.get("resultBody").toString());
		JSONArray jsonResultArray = jsonObj.getJSONObject("hits").getJSONArray("hits");
		
		List<PersonResponse> response = new ArrayList<>();
		JSONObject obj = null;
		JSONObject responseJsonObj = null;
		for(int i=0; i < jsonResultArray.length(); i++) {
			
			obj = (JSONObject) jsonResultArray.get(i);
			responseJsonObj = (JSONObject) obj.get("_source");
			
			response.add(PersonResponse.builder()
										.name(responseJsonObj.get("name").toString())
										.email(responseJsonObj.get("email").toString())
										.nation(responseJsonObj.get("nation").toString())
										.age(responseJsonObj.getInt("age"))
										.gender(responseJsonObj.get("gender").toString())
										.hobby(responseJsonObj.get("hobby").toString())
//										.joindate(responseJsonObj.get("joindate").toString())
										.build());
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
