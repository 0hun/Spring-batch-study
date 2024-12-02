package com.schooldevops.springbatch.batchsample.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schooldevops.springbatch.batchsample.dto.CustomerDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomService {

	public Map<String, String> processToOtherService(CustomerDto item) {

		log.info("Call API to OtherService....");

		return Map.of("code", "200", "message", "OK");
	}
}
