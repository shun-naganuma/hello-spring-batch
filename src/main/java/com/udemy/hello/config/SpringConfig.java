package com.udemy.hello.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.udemy.hello.validator.HelloJobParametersValidator;

@Configuration
public class SpringConfig {
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("HelloTasklet1")
	private Tasklet helloTasklet1;

	@Autowired
	@Qualifier("HelloTasklet2")
	private Tasklet helloTasklet2;

	public SpringConfig(JobLauncher jobLauncher, JobRepository jobRepository,
			PlatformTransactionManager transactionManager) {
		this.jobLauncher = jobLauncher;
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	public JobParametersValidator jobParametersValidator() {
		return new HelloJobParametersValidator();
	}

	@Bean
	public Step helloTaskletStep1() {
		return new StepBuilder("helloTasklet1Step", jobRepository)
				.tasklet(helloTasklet1, transactionManager)
				.build();
	}

	@Bean
	public Step helloTaskletStep2() {
		return new StepBuilder("helloTasklet2Step", jobRepository)
				.tasklet(helloTasklet2, transactionManager)
				.build();
	}

	@Bean
	public Job helloJob() {
		return new JobBuilder("helloJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(helloTaskletStep1())
				.next(helloTaskletStep2())
				.validator(jobParametersValidator())
				.build();
	}
}
