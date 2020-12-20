package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.plugin.core.DefaultPluginEngine;
import com.app.plugin.core.PluginEngine;
import com.app.plugin.core.PluginParameter;

@Configuration
public class PluginConfiguration {

	@Bean
	public PluginEngine plugin() {
		PluginParameter pluginParameter = new PluginParameter();
		pluginParameter.setPathPlugin("plugin/reporting");
		DefaultPluginEngine defaultPluginEngine = new DefaultPluginEngine();
		defaultPluginEngine.build(pluginParameter);
		defaultPluginEngine.reload();
		return defaultPluginEngine;
	}
}
