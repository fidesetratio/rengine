package com.app.plugin.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Plugin {
		private String pluginName;
		private String version;
		private String className;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

		private String description;
		
		public String getFullPath() {
			return fullPath;
		}
		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}

		private String fullPath;
		public String getFolderName() {
			return folderName;
		}
		public void setFolderName(String folderName) {
			this.folderName = folderName;
		}

		private String folderName;
		private Object object;
		public String getPluginName() {
			return pluginName;
		}
		public void setPluginName(String pluginName) {
			this.pluginName = pluginName;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("className: "+className);
			builder.append(" ");
			builder.append("pluginName: "+pluginName);
			builder.append(" ");
			builder.append("version: "+version);
			builder.append(" ");
			return builder.toString();
		}
		public Object getObject() {
			return object;
		}
		public void setObject(Object object) {
			this.object = object;
		}
		
		public void buildObject() {
			ObjectMapper mapper = new ObjectMapper();
			String carAsString = null;
			try {
				carAsString = mapper.writeValueAsString(this.object);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("object To String"+carAsString);
			try {
				this.object = mapper.readValue(carAsString, Class.forName(this.className));
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
}
