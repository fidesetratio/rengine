package com.app.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.app.model.Table;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class QueryServices {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public Table query(String query){
		Table tables = new Table();
		
		List<Map<String,Object>> s = jdbcTemplate.queryForList(query);
		log.info("size:"+s.size());
		if(s.size()>0) {
			tables.setTotal(s.size());
			Map<String,Object> m = s.get(0);
			List<String> headers = m.keySet().stream().collect(Collectors.toList());
			tables.setHeaders(headers);
			tables.setLists(s);
			
		}
		
		return tables;
	}
}
