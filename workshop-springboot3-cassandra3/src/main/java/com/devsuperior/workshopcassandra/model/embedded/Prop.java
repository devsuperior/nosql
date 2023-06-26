package com.devsuperior.workshopcassandra.model.embedded;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.devsuperior.workshopcassandra.model.enums.PropType;

@UserDefinedType("prop")
public class Prop {

	private String name;
	private String value;
	private PropType type;
	
	public Prop() {
	}

	public Prop(String name, String value, PropType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PropType getType() {
		return type;
	}

	public void setType(PropType type) {
		this.type = type;
	}
}
