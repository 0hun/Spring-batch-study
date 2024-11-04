package com.schooldevops.springbatch.batchsample.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Customer {

	private String name;
	private int age;
	private String gender;

	public Customer() {
	}

}
