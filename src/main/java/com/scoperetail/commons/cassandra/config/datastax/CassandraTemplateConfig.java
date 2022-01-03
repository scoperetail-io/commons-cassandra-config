package com.scoperetail.commons.cassandra.config.datastax;

/*-
 * *****
 * commons-cassandra-config
 * -----
 * Copyright (C) 2018 - 2022 Scope Retail Systems Inc.
 * -----
 * This software is owned exclusively by Scope Retail Systems Inc.
 * As such, this software may not be copied, modified, or
 * distributed without express permission from Scope Retail Systems Inc.
 * =====
 */

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;

/**
 * Creating CassandraAdminTemplate bean separately to avoid circular dependency in
 * DataStaxCassandraConfig class
 */
@Configuration
@ConditionalOnProperty(value = "cassandra.db.type", havingValue = "astra", matchIfMissing = false)
public class CassandraTemplateConfig {

  @Autowired CqlSession cqlSession;

  @Bean
  public CassandraAdminTemplate cassandraTemplate() {
    return new CassandraAdminTemplate(cqlSession);
  }
}
