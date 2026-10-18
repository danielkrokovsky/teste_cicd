package com.techchallenge.oficina.support;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Sobe um PostgreSQL descartável (Testcontainers) e o conecta automaticamente ao DataSource.
 * Uso: {@code @Import(PostgresTestConfiguration.class)} em qualquer teste de integração.
 */
@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "testcontainers.enabled", havingValue = "true", matchIfMissing = true)
public class PostgresTestConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		return new PostgreSQLContainer("postgres:17-alpine");
	}
}
