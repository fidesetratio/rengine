package com.app.plugin.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.plugin.core.model.Plugin;
import com.app.plugin.core.model.Reporting;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPluginEngine implements PluginEngine {
	private static Logger logger = LoggerFactory.getLogger(DefaultPluginEngine.class);
	
	private PluginParameter pluginParameter;
	
	private ConcurrentHashMap<String,Plugin> registryPlugin;
	
	public DefaultPluginEngine() {
		this.pluginParameter = new PluginParameter();
		this.registryPlugin = new ConcurrentHashMap<String, Plugin>();
	}

	@Override
	public void build(PluginParameter parameter) {
		// TODO Auto-generated method stub
		this.pluginParameter = parameter;
	}

	@Override
	public List<Plugin> list() {
		// TODO Auto-generated method stub
		return  this.registryPlugin.values().stream().collect(Collectors.toList());
	}

	@Override
	public Plugin getByName(String name) {
		// TODO Auto-generated method stub
		return this.registryPlugin.get(name);
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		String folderName = this.pluginParameter.getPathPlugin();
		File file = new File(folderName);
		if(!file.isDirectory()) {
			logger.info("reload is failed due to directory missing..");
			return;
		}
		String listFile = file.getAbsolutePath()+"/lists.txt";
		try {
			List<String> pluginDirectories = Files.readAllLines(Paths.get(listFile));
			for(String pluginName:pluginDirectories) {
				File d = new File(folderName+"/"+pluginName);
				logger.info("plugin name:"+pluginName );

				if(d.isDirectory()) {
					logger.info("plugin name:"+pluginName + "with directory exists");
					Plugin plugin = plugin(folderName, pluginName);
					if(plugin != null)
					registryPlugin.putIfAbsent(pluginName, plugin);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
		
	}
	
	private Plugin plugin(String pathFile,String name) {
		
		ObjectMapper mapper = new ObjectMapper();
		Plugin plugin =null;
		try {
			plugin = mapper.readValue(new File(pathFile+"/"+name+"/plugin.json"), Plugin.class);
			plugin.setFolderName(name);
			plugin.setFullPath(pathFile+"/"+name);
			plugin.buildObject();
			if(plugin.getObject() instanceof Reporting) {
				Reporting reporting = (Reporting) plugin.getObject();
				reporting.setPlugin(plugin);
			}
			System.out.println("plugin:"+plugin);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return plugin;
	}


}
