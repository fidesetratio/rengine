package com.app.controller;
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
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.QueryModel;
import com.app.model.Table;
import com.app.plugin.core.TaskTable;
import com.app.services.QueryServices;

@RestController
@RequestMapping("/plugin")
public class PluginRest {
	
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

	@PostMapping(path = "/query", consumes = "application/json", produces = "application/json")
	public Table query(@RequestBody QueryModel qmodel) {
	    Table table = new Table();
		String query = qmodel.getQuery();
		table = queryServices.query(query);
		return table;
		
	}
	
	
	@GetMapping("/executeTask")
    public String executeTask() {
		 	/*
			 * ItemReader reader = (ItemReader)context.getBean("pagingItemReaderBean");
			 * ItemWriter writer = (ItemWriter)context.getBean("pagingItemWriterBean");
			 */
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

	        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
	        paramsBuilder.addString("selectQuery", "select rownum as number1,a.reg_spaj");
	        paramsBuilder.addString("fromQuery", "FROM eka.mst_policy a");
	        paramsBuilder.addString("whereQuery", "where 1=1");
	        
	        
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
		  
			/*
			 * Step step = stepBuilderFactory.get("orderStep1").<String, String> chunk(1)
			 * .reader(reader1(null)) .writer(writer1(null)).build();
			 */
	    return "executeTask";
    }
}
