package com.app.plugin.core;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.jdbc.core.RowMapper;

public class JdbcPagingItemTaskReader extends JdbcPagingItemReader<TaskTable> implements StepExecutionListener {
	
	private DataSource ds;
	private int pageSize;
	private RowMapper<TaskTable> mapper;
	public JdbcPagingItemTaskReader(DataSource ds,int pageSize,RowMapper<TaskTable> mapper) {
		super();
		this.ds = ds;
		this.mapper = mapper;
		this.pageSize = pageSize;
		setDataSource(ds);
		System.out.println(getExecutionContextKey("selectQuery"));

		setPageSize(pageSize);
		setQueryProvider(null);
		setRowMapper(mapper);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Step Execution...");
		
		PagingQueryProvider queryProvider=null;
		String selectQuery = stepExecution.getJobExecution().getJobParameters().getString("selectQuery");
		String fromQuery = stepExecution.getJobExecution().getJobParameters().getString("fromQuery");
		String whereQuery = stepExecution.getJobExecution().getJobParameters().getString("whereQuery");
		try {
			queryProvider = queryProvider(ds,selectQuery,fromQuery,whereQuery).getObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDataSource(ds);
		setPageSize(pageSize);
		setRowMapper(mapper);
		setQueryProvider(queryProvider);
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	 private SqlPagingQueryProviderFactoryBean queryProvider(DataSource dataSource,String selectQuery, String fromQuery,String whereQuery) {
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
}
