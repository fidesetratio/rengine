package com.app.plugin.core;

import java.util.HashMap;

public class TaskTable {

		private HashMap<String,Object> results;
		
		public TaskTable() {
			this.results = new HashMap<String, Object>();
		}
	
		public String getReg_spaj() {
		return reg_spaj;
	}

	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}

		private String reg_spaj;
		
 public void add(String key, Object object) {
	 this.results.put(key, object);
 }
 public Object get(String key) {
	 return this.results.get(key);
 }
 
 public HashMap<String,Object> getResults(){
	 return this.results;
 }
		
		
}
