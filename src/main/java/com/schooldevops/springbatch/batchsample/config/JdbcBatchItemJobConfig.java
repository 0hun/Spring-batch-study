package com.schooldevops.springbatch.batchsample.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcBatchItemJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String JDBC_BATCH_WRITER_CHUNK_JOB = "JDBC_BATCH_WRITER_CHUNK_JOB";

	private final DataSource dataSource;

	@Bean
	public FlatFileItemReader<Customer> jdbcFlatFileItemReader() {

		return new FlatFileItemReaderBuilder<Customer>()
			.name("FlatFileItemReader")
			.resource(new ClassPathResource("./customer.csv"))
			.encoding(ENCODING)
			.delimited().delimiter(",")
			.names("name", "age", "gender")
			.targetType(Customer.class)
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<Customer> jdbcFlatFileItemWriter() {

		return new JdbcBatchItemWriterBuilder<Customer>()
			.dataSource(dataSource)
			.sql("INSERT INTO customer2 (name, age, gender) VALUES (:name, :age, :gender)")
			.itemSqlParameterSourceProvider(new CustomerItemSqlParameterSourceProvider())
			.build();
	}


	@Bean
	public Step jdbcFlatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("jdbcFlatFileStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(jdbcFlatFileItemReader())
			.writer(jdbcFlatFileItemWriter())
			.build();
	}

	@Bean
	public Job jdbcBatchWriterChunkJob(@Qualifier("jdbcFlatFileStep") Step flatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");
		return new JobBuilder(JDBC_BATCH_WRITER_CHUNK_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(flatFileStep)
			.build();
	}
}
