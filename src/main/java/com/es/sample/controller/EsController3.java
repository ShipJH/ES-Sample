package com.es.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EsController3 {

	@RequestMapping(value = "/self", method = RequestMethod.GET)
    public String self() {
    		
    	return "views/es-sample";
    }
	
	
	
}
