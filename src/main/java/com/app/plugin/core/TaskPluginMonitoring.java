package com.app.plugin.core;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.app.plugin.core.model.TaskMonitoring;

public class TaskPluginMonitoring {
	
	private ConcurrentHashMap<String,TaskMonitoring> tableMonitoring;
	
	public TaskPluginMonitoring() {
		this.tableMonitoring = new ConcurrentHashMap<String, TaskMonitoring>();
	}
	
	public void put(TaskMonitoring task) {
		tableMonitoring.putIfAbsent(task.getKeyId(), task);
	}
	
	public TaskMonitoring get(String keyId) {
		return tableMonitoring.get(keyId);
		
	}
	
	public void delete(String keyId) {
		tableMonitoring.remove(keyId);
	}
	
	public List<TaskMonitoring> list(){
		return tableMonitoring.values().stream().collect(Collectors.toList());
	}

}
