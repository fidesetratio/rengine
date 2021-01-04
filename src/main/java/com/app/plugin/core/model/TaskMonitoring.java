package com.app.plugin.core.model;

import java.util.Date;

public class TaskMonitoring {

	private String keyId;

	private Date dateTime;
	private String pluginName;
	
	
	public String getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	private String downloadFile;
	
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public TaskMonitoring() {
		this.dateTime = new Date();
		this.progress = 0d;
		this.downloadFile = "";
		this.pluginName = "";
		

	}
	 

	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	private int totalCount;
	public Double getProgress() {
		return progress;
	}


	public void setProgress(Double progress) {
		this.progress = progress;
	}

	private Double progress;
	
	
		
	  public TaskMonitoring(String keyId) {
		super();
		this.keyId = keyId;
		this.dateTime = new Date();
		this.progress = 0d;
		this.downloadFile = "";
		this.pluginName = "";
		
		
	}
	  
	  public TaskMonitoring(String keyId,String pluginName) {
			super();
			this.keyId = keyId;
			this.dateTime = new Date();
			this.progress = 0d;
			this.downloadFile = "";
			this.pluginName = pluginName;
			
			
		}


	public String getKeyId() {
			return keyId;
		}

		public void setKeyId(String keyId) {
			this.keyId = keyId;
		}

		public String getPluginName() {
			return pluginName;
		}

		public void setPluginName(String pluginName) {
			this.pluginName = pluginName;
		};
  
}
