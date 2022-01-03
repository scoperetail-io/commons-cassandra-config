package com.scoperetail.commons.cassandra.config;

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

import com.scoperetail.commons.cassandra.config.apache.ApacheCassandraConfig;
import com.scoperetail.commons.cassandra.config.datastax.DataStaxCassandraConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration(exclude = CassandraDataAutoConfiguration.class)
@Import({ApacheCassandraConfig.class, DataStaxCassandraConfig.class})
public class CassandraConfig {}
