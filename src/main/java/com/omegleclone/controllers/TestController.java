package com.omegleclone.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/test-controller")
public class TestController {

	@GetMapping(value = "/test-method")
	public String TestMethod() {
		return "working";
	}
}
