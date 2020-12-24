package com.app.plugin.core.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Reporting {
	
	private String query;
	private List<Forms> forms;
	private Plugin plugin;

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
					queryText	=  Files.readString(Paths.get(file.getAbsolutePath()));
					queryText   = queryText.trim();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
				 
			 }
		
		}
		return queryText;
	}

}
