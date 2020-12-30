package com.app.plugin.core.model;

public class TaskMonitoring {

	private String keyId;
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
	 public TaskMonitoring() {
		 
	 }
	
		
	  public TaskMonitoring(String keyId) {
		super();
		this.keyId = keyId;
	}

	public String getKeyId() {
			return keyId;
		}

		public void setKeyId(String keyId) {
			this.keyId = keyId;
		};
  
}
