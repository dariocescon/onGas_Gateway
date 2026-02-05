package com.aton.proj.oneGasGateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Main controller per il Gateway
 */
@RestController
public class GatewayController {
	
	@Value("${server.port}")
	private int serverPort;

    @GetMapping("/")
    public Mono<Map<String, String>> responder() {
        return Mono.just(Map.of(
            "status", "UP",
            "service", "onGas_Gateway"
        ));
    }
    
//    @GetMapping("/status")
//	public String status() {
//
//		return String.valueOf(serverPort);
//	}
}