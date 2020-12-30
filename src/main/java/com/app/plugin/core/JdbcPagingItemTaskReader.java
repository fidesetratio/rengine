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

public class JdbcPagingItemTaskReader extends JdbcPagingItemReader<TaskTable> {
	
	public JdbcPagingItemTaskReader(DataSource ds,int pageSize,RowMapper<TaskTable> mapper,PagingQueryProvider queryProvider) {
		super();
		setDataSource(ds);
		setPageSize(pageSize);
		setQueryProvider(queryProvider);
		setRowMapper(mapper);
		try {
			this.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
