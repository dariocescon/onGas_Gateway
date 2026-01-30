# onGas_Gateway 🚀

API Gateway per il sistema onGas, costruito con Spring Cloud Gateway.
Instrada migliaia di richieste verso i servizi **onGas_Commander** con load balancing automatico.

## 🎯 Obiettivi

- **Single Entry Point**: Punto di accesso unico per tutti i client
- **Load Balancing**: Distribuzione automatica del carico tra istanze multiple di onGas_Commander
- **Scalabilità**: Gestione di migliaia di richieste simultanee
- **Monitoraggio**: Logging centralizzato con Log4j2 e metrics con Actuator

## 🏗️ Architettura

```
Client → Gateway (8080) → Load Balancer → onGas_Commander (8081, 8082, 8083, ...)
```

## 📋 Prerequisiti

- **Java 21** (JDK)
- **Maven 3.8+**
- Istanze di **onGas_Commander** in esecuzione

## 🚀 Come Avviare

### 1. Build del progetto

```bash
mvn clean install
```

### 2. Avvio del Gateway

```bash
mvn spring-boot:run
```

Oppure:

```bash
java -jar target/ongas-gateway-1.0.0.jar
```

### 3. Verifica

Il Gateway sarà disponibile su: `http://localhost:8080`

Actuator (monitoraggio): `http://localhost:9090/actuator`

## 📝 Configurazione

### Route Configurate

| Route | Descrizione | Destinazione |
|-------|-------------|--------------|
| `/api/**` | Tutte le API | onGas_Commander (load balanced) |
| `/health` | Health check del gateway | Gateway stesso |

### Istanze onGas_Commander

Configurate in `application.properties`:

```properties
# Istanza 1: localhost:8081
# Istanza 2: localhost:8082
# Istanza 3: localhost:8083
```

Per aggiungere più istanze, modifica il file properties aggiungendo:

```properties
spring.cloud.loadbalancer.clients.ongas-commander.instances[3].instance-id=commander-4
spring.cloud.loadbalancer.clients.ongas-commander.instances[3].host=localhost
spring.cloud.loadbalancer.clients.ongas-commander.instances[3].port=8084
```

## 🧪 Test

### Test delle Route

```bash
# Chiamata che verrà instradata a onGas_Commander
curl http://localhost:8080/api/status

# Health check del gateway
curl http://localhost:8080/health
```

### Monitoraggio

```bash
# Stato di salute
curl http://localhost:9090/actuator/health

# Route configurate
curl http://localhost:9090/actuator/gateway/routes

# Metriche
curl http://localhost:9090/actuator/metrics
```

## 📊 Logging

I log sono salvati nella directory `logs/`:

- `ongas-gateway.log` - Log dell'applicazione
- `access.log` - Log degli accessi
- `error.log` - Solo errori

Configurazione in `log4j2.xml`.

## 🔧 Configurazioni Avanzate

### Timeout

```properties
# Timeout connessione (5 secondi)
spring.cloud.gateway.httpclient.connect-timeout=5000

# Timeout risposta (10 secondi)
spring.cloud.gateway.httpclient.response-timeout=10s
```

### Pool di Connessioni

```properties
# Max connessioni
spring.cloud.gateway.httpclient.pool.max-connections=500
```

### CORS

Già abilitato per tutti gli origin. Modifica in `application.properties` se necessario.

## 📂 Struttura Progetto

```
onGas_Gateway/
├── src/main/java/com/aton/proj/onGasGateway/
│   ├── OnGasGatewayApplication.java    # Classe principale
│   └── config/
│       └── GatewayConfig.java          # Configurazione route
├── src/main/resources/
│   ├── application.properties          # Configurazione
│   └── log4j2.xml                      # Configurazione logging
└── pom.xml                             # Dipendenze Maven
```

## 🛠️ Tecnologie Utilizzate

- **Spring Boot 4.0.1**
- **Spring Cloud Gateway 2025.1.0**
- **Java 21**
- **Maven**
- **Log4j2**

## 📈 Prossimi Passi

1. **Creare il progetto onGas_Commander**
2. Avviare multiple istanze di onGas_Commander su porte diverse (8081, 8082, ecc.)
3. Configurare Service Discovery (Eureka/Consul) per discovery automatico
4. Aggiungere autenticazione/autorizzazione (OAuth2, JWT)
5. Implementare Circuit Breaker (Resilience4j)
6. Configurare Rate Limiting

## 📞 Supporto

Per domande o problemi, contatta il team di sviluppo onGas.