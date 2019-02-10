package com.staroot.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staroot.client.domain.User;

@Controller
public class HomeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/user")
	public String user(Map<String, Object> model) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = "http://localhost:8080/api/users/1";
		ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
		System.out.println("response : "+response);
		System.out.println("response.getHeaders() : "+response.getHeaders());
		System.out.println("response.getBody() : "+response.getBody());
		//assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		
		//Using JSON Data
		//--------------------------------------------------------------
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode userId = root.path("userId");		
		JsonNode name = root.path("name");		
		JsonNode email = root.path("email");		

		logger.debug("===================================");
		System.out.println("userId : "+userId);
		System.out.println("name : "+name);
		System.out.println("email : "+email);
		logger.debug("===================================");
		
		model.put("userId", userId);
		model.put("name", name);
		model.put("email", email);
		//--------------------------------------------------------------
		
		
		//Using POJO Bean
		//--------------------------------------------------------------
		User user = restTemplate.getForObject(resourceUrl, User.class);
		logger.debug("===================================");
		System.out.println("Using POJO Bean");
		System.out.println("user.getName() : "+user.getName());
		System.out.println("user.getEmail() : "+user.getEmail());
		logger.debug("===================================");
		//--------------------------------------------------------------
		
		model.put("title", "TITLE");
		model.put("message", "MESSAGE");
		return "/user";
	}
	@RequestMapping("/search")
	public String search(Map<String, Object> model) throws IOException {
		return "/search";
		
	}
	
	@RequestMapping("/users")
	public String users(Map<String, Object> model) throws IOException {
		RestTemplate restTemplate = new RestTemplate();

		String resourceUrl = "http://localhost:8080/api/users/";

		
		//Check supported options
		//-------------------------------------------------------------------------------------------------
		Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(resourceUrl);
		HttpMethod[] supportedMethod = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};
		//-------------------------------------------------------------------------------------------------

	
		HttpEntity<User> request = new HttpEntity<>(new User());
		//Foo foo = restTemplate.postForObject(resourceUrl, request, Foo.class);
		
		//Using JSON
		//--------------------------------------------------------------
		ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl,request, String.class);
		System.out.println("response : "+response);
		System.out.println("response.getHeaders() : "+response.getHeaders());
		System.out.println("response.getBody() : "+response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		
				
				
		JsonNode root = mapper.readTree(response.getBody());
		
		Iterator itr = root.elements();
		
//		/https://shlee0882.tistory.com/42
		 while( itr.hasNext()) {
	            JsonNode obj = (JsonNode)itr.next(); 
	            System.out.println(obj);
		    		JsonNode userId = obj.path("userId");		
		    		JsonNode name = obj.path("name");		
		    		JsonNode email = obj.path("email");		

		    		logger.debug("===================================");
		    		System.out.println("JsonNode userId : "+userId);
		    		System.out.println("JsonNode name : "+name);
		    		System.out.println("JsonNode email : "+email);
		    		logger.debug("===================================");
	     }
		
		

		
		//--------------------------------------------------------------
		
		//Using POJO Bean
		//--------------------------------------------------------------
		List list = restTemplate.postForObject(resourceUrl, request,ArrayList.class);
		logger.debug("===================================");
		System.out.println("Using POJO Bean");
		System.out.println("list : "+list);
		System.out.println("list.get(0) : "+list.get(0));
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			System.out.println("name::"+map.get("name"));
			System.out.println("email::"+map.get("email"));
			
		}
		logger.debug("===================================");
		//--------------------------------------------------------------
		model.put("name", "TITLE");
		model.put("email", "TITLE");
		
		model.put("title", "TITLE");
		model.put("message", "MESSAGE");
		
		model.put("list", list);
		return "/users";
	}
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("title", "TITLE");
		model.put("message", "MESSAGE");
		logger.debug("=====================HomeController is called=====================");
		return "index";
	}	
	
	// test 5xx errors
	@RequestMapping("/5xx")
	public String ServiceUnavailable() {
		throw new RuntimeException("5xx");
	}	
	// test 4xx errors
	@RequestMapping("/404")
	public String ServiceUnavailable2() {
		throw new RuntimeException("4xx");
	}
	
	/*
	@RequestMapping("/error")
	public String Error() {
		throw new RuntimeException("Error");
	}*/	
}
