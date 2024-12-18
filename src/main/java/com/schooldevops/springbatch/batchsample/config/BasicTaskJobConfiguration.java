package com.schooldevops.springbatch.batchsample.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.schooldevops.springbatch.batchsample.GreetingTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BasicTaskJobConfiguration {

	private final PlatformTransactionManager transactionManager;

	@Bean
	public Tasklet greetingTasklet() {
		return new GreetingTask();
	}

	@Bean
	public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init myStep -----------------");

		return new StepBuilder("myStep", jobRepository).tasklet(greetingTasklet(), transactionManager)
													   .build();
	}

	@Bean
	public Job myJob(@Qualifier("step") Step step, JobRepository jobRepository) {
		log.info("------------------ Init myJob -----------------");
		return new JobBuilder("myJob", jobRepository).incrementer(new RunIdIncrementer())
													 .start(step)
													 .build();
	}

}
