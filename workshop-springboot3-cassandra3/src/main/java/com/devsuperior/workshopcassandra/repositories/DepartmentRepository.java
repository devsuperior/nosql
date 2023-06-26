package com.devsuperior.workshopcassandra.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.devsuperior.workshopcassandra.model.entities.Department;

public interface DepartmentRepository extends CassandraRepository<Department, UUID> {

}
