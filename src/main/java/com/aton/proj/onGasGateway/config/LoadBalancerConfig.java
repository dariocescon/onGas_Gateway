package com.aton.proj.onGasGateway.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import reactor.core.publisher.Flux;

@Configuration
public class LoadBalancerConfig {

	/**
	 * Bean per leggere la configurazione delle istanze da application.properties
	 */
	@Bean
	@ConfigurationProperties(prefix = "meteor")
	public MeteorInstancesConfig meteorInstancesConfig() {
		return new MeteorInstancesConfig();
	}

	/**
	 * ServiceInstanceListSupplier personalizzato che usa la configurazione esterna
	 */
	@Bean
	@Primary
	public ServiceInstanceListSupplier serviceInstanceListSupplier(MeteorInstancesConfig config) {
		return new ServiceInstanceListSupplier() {
			@Override
			public String getServiceId() {
				return "ongas-meteor";
			}

			@Override
			public Flux<List<ServiceInstance>> get() {
				List<ServiceInstance> instances = config.getInstances().stream()
						.map(instance -> new DefaultServiceInstance(instance.getInstanceId(), "ongas-meteor",
								instance.getHost(), instance.getPort(), false // false = HTTP, true = HTTPS
				)).collect(Collectors.toList());

				return Flux.just(instances);
			}
		};
	}

	// Configurazione specifica per ongas-meteor
	public static class MeteorLoadBalancerConfiguration {

		/**
		 * Configura RoundRobin Load Balancer (Default)
		 */
		@Bean
		public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
				LoadBalancerClientFactory loadBalancerClientFactory) {
			String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
			return new RoundRobinLoadBalancer(
					loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
		}
	}

	/**
	 * Classe di configurazione per le istanze Meteor
	 */
	public static class MeteorInstancesConfig {
		private List<InstanceConfig> instances;

		public List<InstanceConfig> getInstances() {
			return instances;
		}

		public void setInstances(List<InstanceConfig> instances) {
			this.instances = instances;
		}
	}

	/**
	 * Configurazione di una singola istanza
	 */
	public static class InstanceConfig {
		private String instanceId;
		private String host;
		private int port;

		public String getInstanceId() {
			return instanceId;
		}

		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
	}
}

//@Configuration
//public class LoadBalancerConfig {
//
//	@Value("${ongas.meteor.instances[0].host:localhost}")
//	private String host1;
//
//	@Value("${ongas.meteor.instances[0].port:8081}")
//	private int port1;
//
//	@Value("${ongas.meteor.instances[1].host:localhost}")
//	private String host2;
//
//	@Value("${ongas.meteor.instances[1].port:8082}")
//	private int port2;
//
//	@Value("${ongas.meteor.instances[2].host:localhost}")
//	private String host3;
//
//	@Value("${ongas.meteor.instances[2].port:8083}")
//	private int port3;
//
//	/**
//	 * Fornisce la lista statica di istanze di onGas_Commander
//	 */
//	@Bean
//	public ServiceInstanceListSupplier serviceInstanceListSupplier() {
//		return new ServiceInstanceListSupplier() {
//			@Override
//			public String getServiceId() {
//				return "ongas-commander";
//			}
//
//			@Override
//			public Flux<List<ServiceInstance>> get() {
//				List<ServiceInstance> instances = new ArrayList<>();
//
//				// Istanza 1
//				instances.add(new DefaultServiceInstance("commander-1", "ongas-meteor", host1, port1, false));
//
//				// Istanza 2
//				instances.add(new DefaultServiceInstance("commander-2", "ongas-meteor", host2, port2, false));
//
//				// Istanza 3
//				instances.add(new DefaultServiceInstance("commander-3", "ongas-meteor", host3, port3, false));
//
//				return Flux.just(instances);
//			}
//		};
//	}
//}