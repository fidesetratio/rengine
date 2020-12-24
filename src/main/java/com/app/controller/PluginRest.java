package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.QueryModel;
import com.app.model.Table;
import com.app.services.QueryServices;

@RestController
@RequestMapping("/plugin")
public class PluginRest {
	
	@Autowired
	private QueryServices queryServices;

	@PostMapping(path = "/query", consumes = "application/json", produces = "application/json")
	public Table query(@RequestBody QueryModel qmodel) {
	    Table table = new Table();
		String query = qmodel.getQuery();
		table = queryServices.query(query);
	
		
		return table;
		
	}
	
}
