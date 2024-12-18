package com.schooldevops.springbatch.batchsample.config.step6;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.schooldevops.springbatch.batchsample.entity.Customer;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaItemJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String JPA_ITEM_WRITER_JOB = "JPA_ITEM_WRITER_JOB";

	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public FlatFileItemReader<Customer> jpaFlatFileItemReader() {

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
	public JpaItemWriter<Customer> jpaItemWriter() {
		return new JpaItemWriterBuilder<Customer>()
			.entityManagerFactory(entityManagerFactory)
			.usePersist(true)
			.build();
	}


	@Bean
	public Step jpaFlatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("jpaFlatFileStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(jpaFlatFileItemReader())
			.writer(jpaItemWriter())
			.build();
	}

	@Bean
	public Job jpaFlatFileJob(@Qualifier("jpaFlatFileStep") Step jpaFlatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");
		return new JobBuilder(JPA_ITEM_WRITER_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(jpaFlatFileStep)
			.build();
	}
}
