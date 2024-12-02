package com.schooldevops.springbatch.batchsample.config.step9;

import javax.sql.DataSource;

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

import com.schooldevops.springbatch.batchsample.config.step6.CustomerItemProcessor;
import com.schooldevops.springbatch.batchsample.entity.Customer;
import com.schooldevops.springbatch.batchsample.entity.QCustomer;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueryDSLPagingReaderJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 2;
	public static final String ENCODING = "UTF-8";
	public static final String QUERYDSL_PAGING_CHUNK_JOB = "QUERYDSL_PAGING_CHUNK_JOB";

	private final DataSource dataSource;
	private final EntityManagerFactory entityManagerFactory;

	//    @Bean
	//    public QuerydslPagingItemReader<Customer> customerQuerydslPagingItemReader() throws Exception {
	//
	//        Function<JPAQueryFactory, JPAQuery<Customer>> query = jpaQueryFactory -> jpaQueryFactory.select(QCustomer.customer).from(QCustomer.customer);
	//
	//        return new QuerydslPagingItemReader<>("customerQuerydslPagingItemReader", entityManagerFactory, query, CHUNK_SIZE, false);
	//    }


	@Bean
	public QuerydslPagingItemReader<Customer> customerQuerydslPagingItemReader() {
		return new QuerydslPagingItemReaderBuilder<Customer>()
			.name("customerQuerydslPagingItemReader")
			.entityManagerFactory(entityManagerFactory)
			.chunkSize(2)
			.querySupplier(jpaQueryFactory -> jpaQueryFactory.select(QCustomer.customer).from(QCustomer.customer).where(QCustomer.customer.age.gt(20)))
			.build();
	}

	@Bean
	public FlatFileItemWriter<Customer> customerQuerydslFlatFileItemWriter() {

		return new FlatFileItemWriterBuilder<Customer>()
			.name("customerQuerydslFlatFileItemWriter")
			.resource(new FileSystemResource("./customer_new_v2.csv"))
			.encoding(ENCODING)
			.delimited().delimiter("\t")
			.names("Name", "Age", "Gender")
			.build();
	}


	@Bean
	public Step customerQuerydslPagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
		log.info("------------------ Init customerQuerydslPagingStep -----------------");

		return new StepBuilder("customerJpaPagingStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerQuerydslPagingItemReader())
			.processor(new CustomerItemProcessor())
			.writer(customerQuerydslFlatFileItemWriter())
			.build();
	}

	@Bean
	public Job customerQueryDslJpaPagingJob(@Qualifier("customerQuerydslPagingStep") Step customerJdbcPagingStep, JobRepository jobRepository) {
		log.info("------------------ Init customerQueryDslJpaPagingJob -----------------");
		return new JobBuilder(QUERYDSL_PAGING_CHUNK_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(customerJdbcPagingStep)
			.build();
	}

}
