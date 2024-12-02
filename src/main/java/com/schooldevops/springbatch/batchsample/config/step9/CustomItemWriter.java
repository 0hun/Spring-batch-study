package com.schooldevops.springbatch.batchsample.config.step9;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.schooldevops.springbatch.batchsample.dto.CustomerDto;
import com.schooldevops.springbatch.batchsample.service.CustomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<CustomerDto> {

	private final CustomService customService;

	public CustomItemWriter(CustomService customService) {
		this.customService = customService;
	}

	@Override
	public void write(Chunk<? extends CustomerDto> chunk) throws Exception {
		for (CustomerDto customer: chunk) {
			log.info("Call Porcess in CustomItemWriter...");
			customService.processToOtherService(customer);
		}
	}
}
