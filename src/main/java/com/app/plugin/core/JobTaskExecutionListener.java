package com.app.plugin.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.services.TaskPluginServices;
@Component(value="jobAndWriteListener")
public class JobTaskExecutionListener implements ItemWriteListener<TaskTable>,JobExecutionListener{
	 private AtomicInteger runningWriteCount = new AtomicInteger(0);
	 private int totalCount;
	    
	 
	@Autowired
	private TaskPluginMonitoring taskPluginMonitoring;
	
	private String keyId;
		
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("before Job"+jobExecution.getJobParameters().getString("total"));
		totalCount = Integer.parseInt(jobExecution.getJobParameters().getString("total"));
		this.keyId = jobExecution.getJobParameters().getString("keyId");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeWrite(List<? extends TaskTable> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterWrite(List<? extends TaskTable> items) {
		// TODO Auto-generated method stub
		
		  double runningWriteCount = this.runningWriteCount.addAndGet(items.size());
		  double percentageComplete = (runningWriteCount / totalCount) * 100;
		  taskPluginMonitoring.get(keyId).setProgress(percentageComplete);
	}

	@Override
	public void onWriteError(Exception exception, List<? extends TaskTable> items) {
		// TODO Auto-generated method stub
		
	}

}
