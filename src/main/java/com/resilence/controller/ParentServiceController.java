package com.resilence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resilence.service.InvokeParentService;

import reactor.core.publisher.Mono;

@RestController
public class ParentServiceController {
	
	@Autowired
	private InvokeParentService jokeService;
	
	@GetMapping("/parent-test")
	public Mono<String> test() {
		return Mono.just("joke");
	}
	
	@GetMapping("/joke")
	public Mono<String> getJoke() {
		return jokeService.getJoke();
	}

}
