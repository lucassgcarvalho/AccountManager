package br.com.enums;

import java.io.Serializable;

public enum Roles implements Serializable {
	
	ADMIN(1,"Admin"), USER(2, "CommonUser");
	
	private Integer id;
	private String value;
	
	Roles(Integer id, String value){
		this.id = id;
		this.value = value;
	}

	public Roles getRole(Integer value){
		return Roles.valueOf(value.toString());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
