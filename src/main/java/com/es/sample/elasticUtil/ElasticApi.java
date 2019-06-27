package com.es.sample.elasticUtil;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class ElasticApi {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    /**
     * 엘라스틱서치에서 제공하는 api를 이용한 전송메소드
     * @param method
     * @param url
     * @param obj
     * @param jsonData
     * @return
     */
    public Map<String, Object> callElasticApi(String method, String url, Object obj, String jsonData) {
        Map<String, Object> result = new HashMap<>();

        String jsonString;
        //json형태의 파라미터가 아니라면 gson으로 만들어주자.
        if (jsonData == null) {
            Gson gson = new Gson();
            jsonString = gson.toJson(obj);
        } else {
            jsonString = jsonData;
        }

        System.out.println(jsonString);
        
        //엘라스틱서치에서 제공하는 restClient를 통해 엘라스틱서치에 접속한다
        try(RestClient restClient = RestClient.builder(new HttpHost(host, port)).build()) {
            
            Request request = new Request(method,url);
            request.addParameter("pretty", "true"); //Map<String, String> params =  Collections.singletonMap("pretty", "true");
            Response response = null; //엘라스틱서치에서 제공하는 response 객체

            //GET, DELETE 메소드는 HttpEntity가 필요없다
            if (method.equals("GET") || method.equals("DELETE")) {
            	
            	response = restClient.performRequest(request); //performRequest(method, url, params);

            } else {
                request.setEntity(new NStringEntity(jsonString, ContentType.APPLICATION_JSON));
            	response = restClient.performRequest(request);
            	//HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
                //response = restClient.performRequest(method, url, params, entity);

            }
            //앨라스틱서치에서 리턴되는 응답코드를 받는다
            int statusCode = response.getStatusLine().getStatusCode();
            //엘라스틱서치에서 리턴되는 응답메시지를 받는다
            String responseBody = EntityUtils.toString(response.getEntity());
            result.put("resultCode", statusCode);
            result.put("resultBody", responseBody);
        } catch (Exception e) {
            result.put("resultCode", -1);
            result.put("resultBody", e.toString());
        }
        return result;
    }
    
    
	/**
	 * GET방식으로 호출하여 ES문법에 맞게끔 넣어줘서 결과값을 반환하는 메서드.
	 * @param url [url 규칙 ex: _search , _cat 등 ..]
	 * @param strJsonParam [json형태의 파라미터를 넘겨야 됩니다.]
	 * @return
	 */
    public Map<String,Object> getSearch(String url, String strJsonParam){
    	
		Map<String, Object> result = new HashMap<>();
    	
        //엘라스틱서치에서 제공하는 restClient를 통해 엘라스틱서치에 접속한다
        try(RestClient restClient = RestClient.builder(new HttpHost(host, port)).build()) {
            
            Request request = new Request("GET",url);
            request.addParameter("pretty", "true"); //Map<String, String> params =  Collections.singletonMap("pretty", "true");
            Response response = null; //엘라스틱서치에서 제공하는 response 객체

            if(strJsonParam != null) {
            	request.setEntity(new NStringEntity(strJsonParam, ContentType.APPLICATION_JSON));	
            }
            
			response = restClient.performRequest(request); 

            //앨라스틱서치에서 리턴되는 응답코드를 받는다
            int statusCode = response.getStatusLine().getStatusCode();
            //엘라스틱서치에서 리턴되는 응답메시지를 받는다
            String responseBody = EntityUtils.toString(response.getEntity());
            result.put("resultCode", statusCode);
            result.put("resultBody", responseBody);
        } catch (Exception e) {
            result.put("resultCode", -1);
            result.put("resultBody", e.toString());
        }
    	
    	return result;
    }
    
    
    
    
    
}




