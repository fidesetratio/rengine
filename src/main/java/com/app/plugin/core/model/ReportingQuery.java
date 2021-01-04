package com.app.plugin.core.model;

public class ReportingQuery {
	private String reportingSelectQuery;
	private String reportingFromQuery;
	private String reportingWhereQuery;
	private String pluginName;
	public String getReportingSelectQuery() {
		return reportingSelectQuery;
	}
	public void setReportingSelectQuery(String reportingSelectQuery) {
		this.reportingSelectQuery = reportingSelectQuery;
	}
	public String getReportingFromQuery() {
		return reportingFromQuery;
	}
	public void setReportingFromQuery(String reportingFromQuery) {
		this.reportingFromQuery = reportingFromQuery;
	}
	public String getReportingWhereQuery() {
		return reportingWhereQuery;
	}
	public void setReportingWhereQuery(String reportingWhereQuery) {
		this.reportingWhereQuery = reportingWhereQuery;
	}
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
}
