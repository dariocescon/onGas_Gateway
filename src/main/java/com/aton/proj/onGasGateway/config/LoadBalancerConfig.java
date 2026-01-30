package com.aton.proj.onGasGateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

/**
 * Configurazione custom del Load Balancer per istanze statiche di
 * onGas_Commander
 */
@Configuration
public class LoadBalancerConfig {

	@Value("${ongas.meteor.instances[0].host:localhost}")
	private String host1;

	@Value("${ongas.meteor.instances[0].port:8081}")
	private int port1;

	@Value("${ongas.meteor.instances[1].host:localhost}")
	private String host2;

	@Value("${ongas.meteor.instances[1].port:8082}")
	private int port2;

	@Value("${ongas.meteor.instances[2].host:localhost}")
	private String host3;

	@Value("${ongas.meteor.instances[2].port:8083}")
	private int port3;

	/**
	 * Fornisce la lista statica di istanze di onGas_Commander
	 */
	@Bean
	public ServiceInstanceListSupplier serviceInstanceListSupplier() {
		return new ServiceInstanceListSupplier() {
			@Override
			public String getServiceId() {
				return "ongas-commander";
			}

			@Override
			public Flux<List<ServiceInstance>> get() {
				List<ServiceInstance> instances = new ArrayList<>();

				// Istanza 1
				instances.add(new DefaultServiceInstance("commander-1", "ongas-meteor", host1, port1, false));

				// Istanza 2
				instances.add(new DefaultServiceInstance("commander-2", "ongas-meteor", host2, port2, false));

				// Istanza 3
				instances.add(new DefaultServiceInstance("commander-3", "ongas-meteor", host3, port3, false));

				return Flux.just(instances);
			}
		};
	}
}