package com.aton.proj.onGasGateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Main controller per il Gateway
 */
@RestController
public class GatewayController {

    @GetMapping("/")
    public Mono<Map<String, String>> responder() {
        return Mono.just(Map.of(
            "status", "UP",
            "service", "onGas_Gateway"
        ));
    }
}