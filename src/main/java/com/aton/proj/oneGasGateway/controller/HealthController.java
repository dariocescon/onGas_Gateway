package com.aton.proj.oneGasGateway.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Health check endpoint per il Gateway
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Mono<Map<String, String>> health() {
        return Mono.just(Map.of(
            "status", "UP",
            "service", "onGas_Gateway"
        ));
    }
}