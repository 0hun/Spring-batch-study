package com.schooldevops.springbatch.batchsample.config.step8;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchsample.dto.CustomerDto;
import com.schooldevops.springbatch.batchsample.entity.Customer;

/**
 * 나이에 20년을 더하는 ItemProcessor
 */
public class After20YearsItemProcessor implements ItemProcessor<Customer, CustomerDto> {
	@Override
	public

	CustomerDto process(Customer item) throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setAge(item.getAge() + 20);
		return customerDto;
	}
}
