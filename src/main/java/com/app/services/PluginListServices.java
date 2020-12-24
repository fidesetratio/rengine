package com.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.plugin.core.PluginEngine;
import com.app.plugin.core.model.Plugin;

@Service
public class PluginListServices {

	@Autowired
	PluginEngine pluginEngine;
	
	public List<Plugin> list(){
		return pluginEngine.list();
	}
	
	
	public Plugin getByName(String name) {
		return pluginEngine.getByName(name);
	}

	
}
