package com.schooldevops.springbatch.batchsample.config;

import org.springframework.batch.item.ItemProcessor;

/**
 * 나이에 20년을 더하는 ItemProcessor
 */
public class After20YearsItemProcessor implements ItemProcessor<Customer, Customer> {
	@Override
	public Customer process(Customer item) throws Exception {
		item.setAge(item.getAge() + 20);
		return item;
	}
}
