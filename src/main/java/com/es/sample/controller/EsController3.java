package com.es.sample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.es.sample.request.PersonSaveReq;

@Controller
public class EsController3 {

	@GetMapping("/testttt")
	public ResponseEntity<?> insertPerson(@RequestBody PersonSaveReq personSaveReq){

		return new ResponseEntity<>("zzz", HttpStatus.OK);
	}
	
}
