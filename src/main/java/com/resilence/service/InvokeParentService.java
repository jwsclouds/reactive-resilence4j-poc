package com.resilence.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.Delimiter;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class InvokeParentService {
	
	@Autowired
	private  WebClient webclient;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private  ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;
	
	
	public Mono<String> getJoke(){
//		return webclient.get().uri("/joke-service/joke").retrieve().bodyToMono(String.class).map(response -> {
//			return response;
//		});
		
		return webclient.get().uri("/joke-service/joke").retrieve().bodyToMono(String.class).map(response -> {
			return response;
		}).timeout(Duration.ofMillis(1000)).transform(it -> {
            ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("customer-service");
            return rcb.run(it, throwable -> fallbackMethod(throwable));
        });

	}
	
    public Mono<String> fallbackMethod(Throwable ex) {
        return Mono.just("This is joke from fallback!!" +System.lineSeparator()+ ex.getMessage() );
    }

}
