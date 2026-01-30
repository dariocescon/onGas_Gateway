package com.aton.proj.onGasGateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayLoggingFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(GatewayLoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		long startTime = System.currentTimeMillis();

		String requestPath = exchange.getRequest().getPath().toString();
		String requestMethod = exchange.getRequest().getMethod().toString();
		String remoteAddress = exchange.getRequest().getRemoteAddress() != null
				? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
				: "unknown";

		logger.info("🚀 Incoming request: {} {} from {}", requestMethod, requestPath, remoteAddress);

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			long duration = System.currentTimeMillis() - startTime;
			int statusCode = exchange.getResponse().getStatusCode() != null
					? exchange.getResponse().getStatusCode().value()
					: 0;

			logger.info("✅ Response: {} {} - Status: {} - Duration: {}ms", requestMethod, requestPath, statusCode,
					duration);
		}));
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}