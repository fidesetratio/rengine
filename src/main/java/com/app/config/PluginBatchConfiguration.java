package com.app.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.app.plugin.core.LoggingItemWriter;
import com.app.plugin.core.TaskTable;

@Configuration
public class PluginBatchConfiguration  {
	 private static final Logger logger = LoggerFactory.getLogger(PluginBatchConfiguration.class);
		
		
	 @Bean
	 public BatchConfigurer configurer(){
	     return new CustomBatchConfiguration();
	 }
		
		
		@Bean(name="pagingItemReaderBean")
		@StepScope
	    public ItemReader<TaskTable> jdbcPaginationItemReader(DataSource dataSource,@Value("#{jobParameters['selectQuery']}") String selectQuery,@Value("#{jobParameters['fromQuery']}") String fromQuery,@Value("#{jobParameters['whereQuery']}") String whereQuery) {
	      logger.info("selectQuery:"+selectQuery);
			PagingQueryProvider queryProvider=null;
			try {
				queryProvider = queryProvider(dataSource,selectQuery,fromQuery,whereQuery).getObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new JdbcPagingItemReaderBuilder<TaskTable>()
	                .name("pagingItemReader")
	                .dataSource(dataSource)
	                .pageSize(100)
	                .queryProvider(queryProvider)
	                .rowMapper(new RowMapper<TaskTable>() {
						
						@Override
						public TaskTable mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							TaskTable table = new TaskTable();
							table.setReg_spaj(rs.getString("reg_spaj"));
							return table;
						}
					})
	                .build();
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
		  
			@Bean(name="pagingItemWriterBean")
			@StepScope
		    public ItemWriter<TaskTable> jdbcPaginationItemWriter() {
		        return new LoggingItemWriter();
		    }
}
