package com.schooldevops.springbatch.batchsample.config;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {
	@Override
	public Customer process(Customer item) throws Exception {
		log.info("Item Processor ------------------- {}", item);
		return item;
	}
}
