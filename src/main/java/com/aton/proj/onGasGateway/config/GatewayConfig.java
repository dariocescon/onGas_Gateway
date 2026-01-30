package com.aton.proj.onGasGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione delle route per il Gateway
 */
@Configuration
public class GatewayConfig {

	/**
	 * Configurazione programmatica delle route (opzionale) Le route sono
	 * principalmente configurate in application.properties
	 */
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// Esempio di route programmatica (commentata)
				// .route("commander-route", r -> r
				// .path("/api/**")
				// .filters(f -> f.stripPrefix(1))
				// .uri("lb://ongas-commander"))
				.build();
	}
}