package com.app.services;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.plugin.core.TaskTable;

@Service
public class TaskPluginServices {
	
	@Autowired
	private QueryServices queryServices;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	
	@Async("processExecutor")
	public void executeTask( JobParametersBuilder paramsBuilder) {
		 ItemReader reader = (ItemReader)context.getBean("pagingItemReaderBean");
		  ItemWriter writer = (ItemWriter)context.getBean("pagingItemWriterBean");
		  Step step = stepBuilderFactory.get("orderStep1").<TaskTable, TaskTable> chunk(100).reader(reader).writer(writer).build();
		  Flow oneFlowOnly = new FlowBuilder<Flow>("singleFlow").
					start(step).
					build();
		  Job job = jobBuilderFactory
	                .get("SimpleJob")
	                .incrementer(new RunIdIncrementer())
	                .start(oneFlowOnly)
	                .end()
	                .build();
		  
		  try {
				JobExecution execution = jobLauncher.run(job,paramsBuilder.toJobParameters());
			} catch (JobExecutionAlreadyRunningException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobRestartException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
