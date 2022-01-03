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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

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
    return cassandraSession;
  }
}
