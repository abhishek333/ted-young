package me.tedyoung.blog.spring_mvc_integration_testing;

import org.hibernate.validator.constraints.NotBlank;

public class SomeEntity {
	@NotBlank
	private String name;
	
	public SomeEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
