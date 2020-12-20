package com.app.plugin.core;

import java.util.List;

import com.app.plugin.core.model.Plugin;

public interface PluginEngine {
	
		public void build(PluginParameter parameter);
		public List<Plugin> list();
		public Plugin getByName(String name);
		public void reload();
}
