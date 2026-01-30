package com.aton.proj.onGasGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route principale per tutte le API verso onGas_Meteor
                .route("meteor_api_route", r -> r
                        .path("/api/**")
                        .filters(f -> f
                                .stripPrefix(1) // Rimuove /api dal path
                                .addRequestHeader("X-Gateway", "onGas-Gateway")
                                .addResponseHeader("X-Powered-By", "onGas-Gateway")
                                .dedupeResponseHeader("Access-Control-Allow-Credentials Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST)
                                )
                        )
                        .uri("lb://ongas-meteor") // Load balanced
                )
                
                // Route per health check specifico di Meteor
                .route("meteor_health_route", r -> r
                        .path("/meteor/health")
                        .filters(f -> f
                                .stripPrefix(1)
                                .addRequestHeader("X-Health-Check", "true")
                                .dedupeResponseHeader("Access-Control-Allow-Credentials Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://ongas-meteor")
                )
                
                // Route per telemetry data
                .route("meteor_telemetry_route", r -> r
                        .path("/telemetry/**")
                        .filters(f -> f
                                .addRequestHeader("X-Data-Type", "Telemetry")
                                .addResponseHeader("X-Service", "Meteor")
                                .dedupeResponseHeader("Access-Control-Allow-Credentials Access-Control-Allow-Origin", "RETAIN_UNIQUE")
                        )
                        .uri("lb://ongas-meteor")
                )
                
                // Route per endpoint di test diretto (senza load balancing)
                .route("meteor_direct_route", r -> r
                        .path("/direct/**")
                        .filters(f -> f
                                .stripPrefix(1)
                        )
                        .uri("http://localhost:8081") // Istanza diretta
                )
                
                .build();
    }
}