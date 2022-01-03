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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.nio.file.Paths;

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

  @Bean
  public CqlSession cqlSession() {
    return CqlSession.builder()
        .withCloudSecureConnectBundle(Paths.get(secureConnectBundlePath))
        .withAuthCredentials(username, password)
        .withKeyspace(keySpace)
        .build();
  }
}
