package com.aton.proj.oneGasGateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		logger.debug("========================================");
		logger.debug("  Creazione customRouteLocator");
		logger.debug("========================================");

		RouteLocator locator = builder.routes()
				// Route principale per tutte le API verso onegas_Meteor
				.route("meteor_api_route", r -> {
					logger.debug("   Configurazione route: meteor_api_route");
					return r.path("/api/**").filters(f -> f.stripPrefix(1)
							.addRequestHeader("X-Gateway", "oneGas-Gateway")
							.addResponseHeader("X-Powered-By", "oneGas-Gateway")
							.dedupeResponseHeader("Access-Control-Allow-Credentials Access-Control-Allow-Origin",
									"RETAIN_UNIQUE")
							.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET, HttpMethod.POST)))
							.uri("lb://onegas-meteor");
				})

				// Route per health check specifico di Meteor
				.route("meteor_health_route", r -> {
					logger.debug("   Configurazione route: meteor_health_route");
					return r.path("/meteor/health").filters(
							f -> f.stripPrefix(1).addRequestHeader("X-Health-Check", "true").dedupeResponseHeader(
									"Access-Control-Allow-Credentials Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
							.uri("lb://onegas-meteor");
				})

				// Route per telemetry data
				.route("meteor_telemetry_route", r -> {
					logger.debug("   Configurazione route: meteor_telemetry_route");
					return r.path("/telemetry/**")
							.filters(f -> f.addRequestHeader("X-Data-Type", "Telemetry")
									.addResponseHeader("X-Service", "Meteor").dedupeResponseHeader(
											"Access-Control-Allow-Credentials Access-Control-Allow-Origin",
											"RETAIN_UNIQUE"))
							.uri("lb://onegas-meteor");
				})

				// Route per endpoint di test diretto (senza load balancing)
				.route("meteor_direct_route", r -> {
					logger.debug("   Configurazione route: meteor_direct_route");
					return r.path("/direct/**").filters(f -> f.stripPrefix(1)).uri("http://localhost:8081");
				})

				.build();

		logger.debug("========================================");
		logger.debug("  customRouteLocator creato");
		logger.debug("========================================");

		return locator;
	}
}