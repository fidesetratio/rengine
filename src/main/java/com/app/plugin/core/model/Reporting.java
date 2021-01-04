package com.app.plugin.core.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class Reporting {
	
	private String query;
	private List<Forms> forms;
	private Plugin plugin;
	
	private String reportingquery;
	private String reportselectQuery;
	private String reportfromQuery;
	private String reportwhereQuery;
	
	private List<Forms> reportingform;
	
	private Boolean allowedQuickSearch;
	private Boolean allowedReporting;
	
	

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Forms> getForms() {
		return forms;
	}

	public void setForms(List<Forms> forms) {
		this.forms = forms;
	}
	
	public boolean isFromLoadText()
	{
		return this.query.trim().contains(".txt");
	}
	public String loadQueryFromTxt() {
		String queryText = null;
		if(this.plugin != null) {
			 File file = new File(this.plugin.getFullPath()+"/query.txt");
			 if(file.exists()) {
				 try {
					 
					//queryText	=  Files.readString(Paths.get(file.getAbsolutePath()));
					 queryText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
					 
						
					 queryText   = queryText.trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
		
		}
		return queryText;
	}

	
	public void loadReportQueryFromTxt() {
		String queryText = null;
		String reportSelectQuery = this.reportingquery+"selectQuery.txt";
		String reportFromQuery = this.reportingquery+"fromQuery.txt";
		String reportWhereQuery = this.reportingquery+"whereQuery.txt";
		if(this.plugin != null) {
			 File file = new File(this.plugin.getFullPath()+"/"+reportSelectQuery);
			 if(file.exists()) {
				 try {
					//queryText	=  Files.readString(Paths.get(file.getAbsolutePath()));
					 queryText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

					 
					queryText = StringUtils.normalizeSpace(queryText);
					this.reportselectQuery   = queryText.trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
			 
			 file = new File(this.plugin.getFullPath()+"/"+reportFromQuery);
			 if(file.exists()) {
				 try {
					 
//					queryText	=  Files.readString(Paths.get(file.getAbsolutePath()));
					 queryText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

					queryText = StringUtils.normalizeSpace(queryText);
					
					
					
					this.reportfromQuery   = queryText.trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
		
			 
			 file = new File(this.plugin.getFullPath()+"/"+reportWhereQuery);
			 if(file.exists()) {
				 try {
					//queryText	=  Files.readString(Paths.get(file.getAbsolutePath()));
					 queryText = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

					queryText = StringUtils.normalizeSpace(queryText);
					
					
					this.reportwhereQuery   = queryText.trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
		
		}
		
	}
	
	
	
	
	
	public String getReportingquery() {
		return reportingquery;
	}

	public void setReportingquery(String reportingquery) {
		this.reportingquery = reportingquery;
	}

	public String getReportselectQuery() {
		return reportselectQuery;
	}

	public void setReportselectQuery(String reportselectQuery) {
		this.reportselectQuery = reportselectQuery;
	}

	public String getReportfromQuery() {
		return reportfromQuery;
	}

	public void setReportfromQuery(String reportfromQuery) {
		this.reportfromQuery = reportfromQuery;
	}

	public String getReportwhereQuery() {
		return reportwhereQuery;
	}

	public void setReportwhereQuery(String reportwhereQuery) {
		this.reportwhereQuery = reportwhereQuery;
	}

	public List<Forms> getReportingform() {
		return reportingform;
	}

	public void setReportingform(List<Forms> reportingform) {
		this.reportingform = reportingform;
	}

	public Boolean getAllowedQuickSearch() {
		return allowedQuickSearch;
	}

	public void setAllowedQuickSearch(Boolean allowedQuickSearch) {
		this.allowedQuickSearch = allowedQuickSearch;
	}

	public Boolean getAllowedReporting() {
		return allowedReporting;
	}

	public void setAllowedReporting(Boolean allowedReporting) {
		this.allowedReporting = allowedReporting;
	}

}
