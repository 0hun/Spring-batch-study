package com.schooldevops.springbatch.batchsample.config.step8;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchsample.dto.CustomerDto;
import com.schooldevops.springbatch.batchsample.entity.Customer;

/**
 * 이름, 성별을 소문자로 변경하는 ItemProcessor
 */
public class LowerCaseItemProcessor implements ItemProcessor<Customer, CustomerDto> {
	@Override
	public CustomerDto process(Customer item) throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setName(item.getName().toLowerCase());
		customerDto.setGender(item.getGender().toLowerCase());
		return customerDto;
	}
}
