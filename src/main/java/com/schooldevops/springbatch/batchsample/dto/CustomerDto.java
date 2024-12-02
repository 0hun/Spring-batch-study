package com.schooldevops.springbatch.batchsample.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDto {

	private int id;
	private String name;
	private int age;
	private String gender;

	public CustomerDto() {
	}

	public CustomerDto(int id, String name, int age, String gender) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
}
