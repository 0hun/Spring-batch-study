package com.schooldevops.springbatch.batchsample.config.step9;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.schooldevops.springbatch.batchsample.dto.CustomerDto;
import com.schooldevops.springbatch.batchsample.entity.Customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MybatisItemWriterJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String MY_BATIS_ITEM_WRITER = "MY_BATIS_ITEM_CUSTOM_WRITER";

	private final DataSource dataSource;
	private final SqlSessionFactory sqlSessionFactory;
	private final CustomItemWriter customItemWriter;

	@Bean
	public FlatFileItemReader<CustomerDto> flatFileItemReader9() {

		return new FlatFileItemReaderBuilder<CustomerDto>()
			.name("flatFileItemReader9")
			.resource(new ClassPathResource("./customer.csv"))
			.encoding(ENCODING)
			.delimited().delimiter(",")
			.names("name", "age", "gender")
			.targetType(CustomerDto.class)
			.build();
	}

	@Bean
	public Step flatFileStep9(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("flatFileStep", jobRepository)
			.<CustomerDto, CustomerDto>chunk(CHUNK_SIZE, transactionManager)
			.reader(flatFileItemReader9())
			.writer(customItemWriter)
			.build();
	}

	@Bean
	public Job flatFileJob9(@Qualifier("flatFileStep9") Step flatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");
		return new JobBuilder(MY_BATIS_ITEM_WRITER, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(flatFileStep)
			.build();
	}
}
