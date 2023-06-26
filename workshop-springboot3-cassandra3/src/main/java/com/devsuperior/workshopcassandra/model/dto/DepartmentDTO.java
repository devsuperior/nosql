package com.devsuperior.workshopcassandra.model.dto;

import java.util.UUID;

import com.devsuperior.workshopcassandra.model.entities.Department;

public class DepartmentDTO {

	private UUID id;
	private String name;
	
	public DepartmentDTO() {
	}

	public DepartmentDTO(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public DepartmentDTO(Department entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
