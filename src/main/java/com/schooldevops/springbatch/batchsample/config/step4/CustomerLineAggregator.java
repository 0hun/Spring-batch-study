package com.schooldevops.springbatch.batchsample.config.step4;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.schooldevops.springbatch.batchsample.entity.Customer;

public class CustomerLineAggregator implements LineAggregator<Customer> {
	@Override
	public String aggregate(Customer item) {
		return item.getName() + "," + item.getAge();
	}
}

