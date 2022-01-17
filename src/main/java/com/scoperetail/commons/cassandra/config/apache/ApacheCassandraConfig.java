package com.scoperetail.commons.cassandra.config.apache;

/*-
 * *****
 * commons-cassandra-config
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * This software is owned exclusively by Scope Retail Systems Inc.
 * As such, this software may not be copied, modified, or
 * distributed without express permission from Scope Retail Systems Inc.
 * =====
 */

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "cassandra.db.type", havingValue = "apache", matchIfMissing = false)
public class ApacheCassandraConfig extends AbstractCassandraConfiguration {

  @Value("${spring.data.cassandra.keyspace-name}")
  private String keySpace;

  @Value("${spring.data.cassandra.contact-points}")
  private String contactPoints;

  @Value("${spring.data.cassandra.local-datacenter}")
  private String localDatacenter;

  @Value("${spring.data.cassandra.port}")
  private int port;

  @Value("${spring.data.cassandra.username}")
  private String username;

  @Value("${spring.data.cassandra.password}")
  private String password;

  @Value("${datastax-java-driver.basic.request.timeout:2000}")
  private long requestTimeout;

  @Value("${datastax-java-driver.advanced.metadata.schema.request-timeout:2000}")
  private long schemaRequestTimeout;

  @Value("${datastax-java-driver.advanced.connection.init-query-timeout:5000}")
  private long initQueryTimeout;

  @Value("${datastax-java-driver.advanced.connection.pool.local.size:1}")
  private int connectionPoolLocalSize;

  @Value("${datastax-java-driver.advanced.connection.pool.remote.size:1}")
  private int connectionPoolRemoteSize;

  @Value("${datastax-java-driver.advanced.connection.max-requests-per-connection:1024}")
  private int maxRequestsPerConnection;

  @Override
  protected String getKeyspaceName() {
    return keySpace;
  }

  @Bean
  @Override
  public CqlSessionFactoryBean cassandraSession() {
    CqlSessionFactoryBean cassandraSession = new CqlSessionFactoryBean();
    cassandraSession.setContactPoints(contactPoints);
    cassandraSession.setKeyspaceName(this.getKeyspaceName());
    cassandraSession.setLocalDatacenter(localDatacenter);
    cassandraSession.setPort(port);
    cassandraSession.setUsername(username);
    cassandraSession.setPassword(password);
    cassandraSession.setSessionBuilderConfigurer(getSessionBuilderConfigurer());
    return cassandraSession;
  }

  @Override
  protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
    return new SessionBuilderConfigurer() {

      public CqlSessionBuilder configure(CqlSessionBuilder cqlSessionBuilder) {
        return cqlSessionBuilder.withConfigLoader(
            DriverConfigLoader.programmaticBuilder()
                .withDuration(
                    DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(requestTimeout))
                .withDuration(
                    DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT,
                    Duration.ofMillis(schemaRequestTimeout))
                .withDuration(
                    DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT,
                    Duration.ofMillis(initQueryTimeout))
                .withInt(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, connectionPoolLocalSize)
                .withInt(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, connectionPoolRemoteSize)
                // The maximum number of requests that can be executed concurrently on a connection.
                // This must be between 1 and 32768.
                .withInt(DefaultDriverOption.CONNECTION_MAX_REQUESTS, maxRequestsPerConnection)
                .build());
      }
    };
  }
}
