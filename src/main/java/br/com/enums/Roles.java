package br.com.enums;

import java.io.Serializable;

public enum Roles implements Serializable {
	
	ADMIN(1),USER(2);
	
	private Integer value;
	
	Roles(Integer value){
		this.value = value;
	}

	public Roles getRole(Integer value){
		return Roles.valueOf(value.toString());
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}
