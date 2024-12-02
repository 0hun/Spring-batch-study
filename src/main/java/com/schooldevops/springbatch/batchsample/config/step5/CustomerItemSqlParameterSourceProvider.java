package com.schooldevops.springbatch.batchsample.config.step5;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.schooldevops.springbatch.batchsample.entity.Customer;

public class CustomerItemSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Customer> {
	@Override
	public SqlParameterSource createSqlParameterSource(Customer item) {
		return new BeanPropertySqlParameterSource(item);
	}
}
