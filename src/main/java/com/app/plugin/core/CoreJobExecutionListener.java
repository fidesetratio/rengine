package com.app.plugin.core;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CoreJobExecutionListener implements JobExecutionListener{
	private TaskPluginMonitoring taskPluginMonitoring;
	private String keyId;
	public CoreJobExecutionListener(TaskPluginMonitoring taskPluginMonitoring,String keyId) {
		super();
		this.taskPluginMonitoring = taskPluginMonitoring;
		this.keyId = keyId;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		Integer totalCount  = Integer.parseInt(jobExecution.getJobParameters().getString("total"));
		taskPluginMonitoring.get(keyId).setTotalCount(totalCount);
		System.out.println("Before JOBBB");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}

}
