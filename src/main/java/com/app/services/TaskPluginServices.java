package com.app.services;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
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
			
		
		  String selectQuery =  paramsBuilder.toJobParameters().getString("selectQuery");
		  String fromQuery =  paramsBuilder.toJobParameters().getString("fromQuery");
		  String whereQuery =  paramsBuilder.toJobParameters().getString("whereQuery");
		  
		  List<String> headers = queryServices.getHeaders(getHeaderQuery(selectQuery, fromQuery, whereQuery));
		  Integer total = queryServices.getTotal(getTotalQuery(fromQuery, whereQuery));
		  if(headers.size()>0) {
			  String hString = String.join(";",headers);
			  paramsBuilder.addString("headers",hString);
		  }
		  paramsBuilder.addString("total", Integer.toString(total));
			
		
		  ItemReader reader = (ItemReader)context.getBean("pagingItemReaderBean");
		  ItemWriter writer = (ItemWriter)context.getBean("excelPagingWriterBean");
		  JobExecutionListener jobExecutionListener = (JobExecutionListener) context.getBean("jobAndWriteListener");
		  ItemWriteListener<TaskTable> itemWriteListener = (ItemWriteListener<TaskTable>) context.getBean("jobAndWriteListener");
		  
		  Step step = stepBuilderFactory.get("orderStep1").<TaskTable, TaskTable> chunk(100).reader(reader).writer(writer).listener(itemWriteListener).build();
		  Flow oneFlowOnly = new FlowBuilder<Flow>("singleFlow").
					start(step).
					build();
		  Job job = jobBuilderFactory
	                .get("SimpleJob")
	                .listener(jobExecutionListener)
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
	
	private String getTotalQuery(String fromQuery,String whereQuery) {
		StringBuffer q = new StringBuffer();
		q.append("select count(*) as total");
		q.append(" ");
		q.append(fromQuery);
		q.append(" ");
		q.append(whereQuery);
		return q.toString();
	}

	
	private String getHeaderQuery(String selectQuery, String fromQuery, String whereQuery) {
		StringBuffer q = new StringBuffer();
		q.append(selectQuery);
		q.append(" ");
		q.append(fromQuery);
		q.append(" ");
		q.append(whereQuery);
		q.append(" ");
		q.append(" and rownum = 1");
		return q.toString();
	}
}
