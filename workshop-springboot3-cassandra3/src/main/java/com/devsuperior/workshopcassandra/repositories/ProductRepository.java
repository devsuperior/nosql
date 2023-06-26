package com.devsuperior.workshopcassandra.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.devsuperior.workshopcassandra.model.entities.Product;

public interface ProductRepository extends CassandraRepository<Product, UUID> {

	@AllowFiltering
	List<Product> findByDepartment(String department);
	
	@Query("SELECT * FROM products WHERE description LIKE :text")
	List<Product> findByDescription(String text);
}
