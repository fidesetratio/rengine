package com.app.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.plugin.core.CoreItemWriteListener;
import com.app.plugin.core.CoreJobExecutionListener;
import com.app.plugin.core.ExcellWriter;
import com.app.plugin.core.JdbcPagingItemTaskReader;
import com.app.plugin.core.TaskPluginMonitoring;
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
	
	@Autowired
	private DataSource dataSource;
	
	
	@Async("processExecutor")
	public void executeTask2( JobParametersBuilder paramsBuilder, String keyId,TaskPluginMonitoring taskPluginMonitoring) {
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
		  
		  PagingQueryProvider queryProvider=null;
			try {
				queryProvider = queryProvider(dataSource,selectQuery,fromQuery,whereQuery).getObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  ItemReader reader =  new JdbcPagingItemTaskReader(dataSource, 1000, new RowMapper<TaskTable>() {
						@Override
						public TaskTable mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							TaskTable table = new TaskTable();
							ResultSetMetaData rsmd = rs.getMetaData();
							 int columnCount = rsmd.getColumnCount();
							 for(int i = 1 ; i <= columnCount ; i++){
								 table.add(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
							 }
							table.setReg_spaj(rs.getString("reg_spaj"));
							return table;
						}
					}, queryProvider);
				  
				  
				  
		  
		  
		  ItemWriter writer =    new ExcellWriter(taskPluginMonitoring);
		  
		  JobExecutionListener jobExecutionListener = new CoreJobExecutionListener(taskPluginMonitoring, keyId);
		  
		  
		  ItemWriteListener<TaskTable> itemWriteListener = new CoreItemWriteListener(taskPluginMonitoring, keyId,total);
		  
		  Step step = stepBuilderFactory.get("orderStep1").<TaskTable, TaskTable> chunk(1000).reader(reader).writer(writer).listener(itemWriteListener).build();
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
	
	
	
	
    public SqlPagingQueryProviderFactoryBean queryProvider(DataSource dataSource,String selectQuery, String fromQuery,String whereQuery) {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause(selectQuery);
        queryProvider.setFromClause(fromQuery);
  	    queryProvider.setWhereClause(whereQuery);
        queryProvider.setSortKeys(sortByRegSpaj());
        return queryProvider;
    }


	  private Map<String, Order> sortByRegSpaj() {
	        Map<String, Order> sortConfiguration = new HashMap<>();
	        sortConfiguration.put("number1", Order.DESCENDING);
	        return sortConfiguration;
	   }
	  
	  
	@Async("processExecutor")
	public void executeTask( JobParametersBuilder paramsBuilder, String taskKeyId) {
			
		
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
