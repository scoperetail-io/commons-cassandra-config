package com.scoperetail.commons.cassandra.config.datastax;

/*-
 * *****
 * commons-cassandra-config
 * -----
 * Copyright (C) 2018 - 2022 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====
 */

import java.nio.file.Paths;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

@Configuration
@ConditionalOnProperty(value = "db.type", havingValue = "Astra-Cassandra", matchIfMissing = false)
@Import({AstraCassandraTemplateConfig.class})
public class AstraCassandraConfig {

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
