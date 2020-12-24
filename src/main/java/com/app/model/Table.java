package com.app.model;

import java.util.List;
import java.util.Map;

public class Table {
	    public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

		private Integer total;
	
		public List<String> getHeaders() {
			return headers;
		}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<Map<String, Object>> getLists() {
		return lists;
	}

	public void setLists(List<Map<String, Object>> lists) {
		this.lists = lists;
	}

		private List<String> headers;
		
		private List<Map<String,Object>> lists;

}
