package com.devsuperior.workshopcassandra.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspace;

	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoint;

	@Value("${spring.data.cassandra.port}")
	private int port;
	
	@Value("${spring.data.cassandra.local-datacenter}")
	private String localDatacenter;

	@Override
	public String getContactPoints() {
		return contactPoint;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keyspace).ifNotExists()
				.with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication(1L));
	}

	@Override
	protected String getLocalDataCenter() {
		return localDatacenter;
	}

	@Override
	protected String getKeyspaceName() {
		return keyspace;
	}

	@Override
	public String[] getEntityBasePackages() {
		return new String[] { "com.devsuperior.workshopcassandra.model.entities" };
	}
}
