package com.schooldevops.springbatch.batchsample.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyBatisWriterJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String MYBATIS_CHUNK_JOB = "MY_BATIS_ITEM_WRITER";

	private final DataSource dataSource;
	private final SqlSessionFactory sqlSessionFactory;

	@Bean
	public MyBatisPagingItemReader<Customer> myBatisItemReader2() {

		return new MyBatisPagingItemReaderBuilder<Customer>()
			.sqlSessionFactory(sqlSessionFactory)
			.pageSize(CHUNK_SIZE)
			.queryId("com.schooldevops.springbatch.batchsample.jobs.selectCustomers")
			.build();
	}


	@Bean
	public MyBatisBatchItemWriter<Customer> mybatisItemWriter() {
		return new MyBatisBatchItemWriterBuilder<Customer>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.schooldevops.springbatch.batchsample.jobs.insertCustomers")
			.itemToParameterConverter(item -> {
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("name", item.getName());
				parameter.put("age", item.getAge() + 1);
				parameter.put("gender", item.getGender());
				return parameter;
			})
			.build();
	}

	@Bean
	public Step customerJdbcCursorStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
		log.info("------------------ Init customerJdbcCursorStep -----------------");

		return new StepBuilder("customerJdbcCursorStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(myBatisItemReader2())
			.writer(mybatisItemWriter())
			.build();
	}

	@Bean
	public Job customerJdbcCursorPagingJob2(@Qualifier("customerJdbcCursorStep2") Step customerJdbcCursorStep, JobRepository jobRepository) {
		log.info("------------------ Init customerJdbcCursorPagingJob2 -----------------");
		return new JobBuilder(MYBATIS_CHUNK_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(customerJdbcCursorStep)
			.build();
	}
}
