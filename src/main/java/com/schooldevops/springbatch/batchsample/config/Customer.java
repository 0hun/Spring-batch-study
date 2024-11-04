package com.schooldevops.springbatch.batchsample.config;

import lombok.Getter;

@Getter
public class Customer {

	private String name;
	private int age;
	private String gender;

	public Customer() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
