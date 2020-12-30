package com.app.plugin.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.ItemWriteListener;

import com.app.plugin.core.model.TaskMonitoring;

public class CoreItemWriteListener implements ItemWriteListener<TaskTable>{
	
	private TaskPluginMonitoring taskPluginMonitoring;
	private String keyId;
   private AtomicInteger runningWriteCount = new AtomicInteger(0);
   private Integer totalCount;
	public CoreItemWriteListener(TaskPluginMonitoring taskPluginMonitoring, String keyId,Integer totalCount) {
		this.taskPluginMonitoring = taskPluginMonitoring;
		this.keyId =keyId;
		this.totalCount = totalCount;
		System.out.println("Total Count:"+this.totalCount);
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
