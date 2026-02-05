package com.aton.proj.oneGasGateway.config;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import reactor.core.publisher.Flux;

@Configuration
public class LoadBalancerRoundRobinConfig {

	private static final Logger logger = LoggerFactory.getLogger(LoadBalancerRoundRobinConfig.class);

	/**
	 * Bean per leggere la configurazione delle istanze da application.properties
	 */
	@Bean
	@ConfigurationProperties(prefix = "meteor")
	public MeteorInstancesConfig meteorInstancesConfig() {
		logger.debug("========================================");
		logger.debug("  Creazione MeteorInstancesConfig bean");
		logger.debug("========================================");
		return new MeteorInstancesConfig();
	}

	/**
	 * ServiceInstanceListSupplier personalizzato che usa la configurazione esterna
	 */
	@Bean
	@Primary
	public ServiceInstanceListSupplier serviceInstanceListSupplier(MeteorInstancesConfig config) {

		logger.debug("========================================");
		logger.debug("   Creazione ServiceInstanceListSupplier");
		logger.debug("   Config ricevuto: " + config);
		logger.debug("   Istanze: " + (config.getInstances() != null ? config.getInstances().size() : "NULL"));
		logger.debug("========================================");

		return new ServiceInstanceListSupplier() {
			@Override
			public String getServiceId() {
				return "onegas-meteor";
			}

			@Override
			public Flux<List<ServiceInstance>> get() {

				logger.debug("  ServiceInstanceListSupplier.get() chiamato!");

				if (config.getInstances() == null || config.getInstances().isEmpty()) {
					logger.debug("  ERRORE: config.getInstances() è null o vuoto!");
					return Flux.just(List.of());
				}

				List<ServiceInstance> instances = config.getInstances().stream().map(instance -> {
					logger.debug("  Caricata istanza: " + instance.getInstanceId() + " -> " + instance.getHost() + ":"
							+ instance.getPort());
					return new DefaultServiceInstance(instance.getInstanceId(), "onegas-meteor", instance.getHost(),
							instance.getPort(), false);
				}).collect(Collectors.toList());

				logger.debug("  Totale istanze caricate: " + instances.size());
				return Flux.just(instances);
			}
		};
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