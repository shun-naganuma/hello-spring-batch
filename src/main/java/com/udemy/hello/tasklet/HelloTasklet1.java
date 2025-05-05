package com.udemy.hello.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("HelloTasklet1")
@StepScope
public class HelloTasklet1 implements Tasklet {

	@Value("#{jobParameters['param1']}")
	private String param1;

	@Value("#{jobParameters['param2']}")
	private Integer param2;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("Hello Tasklet1 !!!");
		System.out.println("param1=" + param1);
		System.out.println("param2=" + param2);

		ExecutionContext jobContext = contribution.getStepExecution()
				.getJobExecution()
				.getExecutionContext();
		jobContext.put("jobKey1", "jobValue1aaa");

		return RepeatStatus.FINISHED;
	}

}
