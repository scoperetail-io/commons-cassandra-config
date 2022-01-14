package com.scoperetail.commons.cassandra.config.datastax;

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

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import java.nio.file.Paths;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "cassandra.db.type", havingValue = "astra", matchIfMissing = false)
@Import({CassandraTemplateConfig.class})
public class DataStaxCassandraConfig {

  @Value("${datastax.astra.secure-connect-bundle.path}")
  private String secureConnectBundlePath;

  @Value("${spring.data.cassandra.username}")
  private String username;

  @Value("${spring.data.cassandra.password}")
  private String password;

  @Value("${spring.data.cassandra.keyspace-name}")
  private String keySpace;

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

  @Bean
  public CqlSession cqlSession() {
    return CqlSession.builder()
        .withCloudSecureConnectBundle(Paths.get(secureConnectBundlePath))
        .withAuthCredentials(username, password)
        .withKeyspace(keySpace)
        .withConfigLoader(
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
                // The maximum number of requests that can be executed concurrently on a
                // connection.
                // This must be between 1 and 32768.
                .withInt(DefaultDriverOption.CONNECTION_MAX_REQUESTS, maxRequestsPerConnection)
                .build())
        .build();
  }

  @Bean
  public CassandraConverter cassandraConverter() {
    return new MappingCassandraConverter(this.mappingContext());
  }

  @Bean
  public CassandraMappingContext mappingContext() {
    return new CassandraMappingContext();
  }
}
