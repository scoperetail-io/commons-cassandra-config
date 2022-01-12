package com.scoperetail.commons.cassandra.config.common;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.AsyncCqlTemplate;

@Configuration
public class AsyncTemplateConfig {
  @Autowired CqlSession cqlSession;

  @Bean
  public AsyncCqlTemplate asyncCqlTemplate() {
    return new AsyncCqlTemplate(cqlSession);
  }
}
