package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.plugin.constants.PluginConstants;
import com.app.plugin.core.model.Plugin;
import com.app.plugin.core.model.Reporting;
import com.app.services.PluginListServices;
import com.app.services.QueryServices;

@Controller
public class MainController {
	@Autowired
	private PluginListServices pluginListService;
	  
	@Autowired
	private QueryServices queryServices;
	
		@GetMapping("/")
	    public String main(Model model) {
			List<Plugin> list = pluginListService.list();
			System.out.println(list.size());
		    model.addAttribute("plugins", list );
	        return "main";
	    }
		
		@GetMapping("/pluginview")
	    public String pluginview(@RequestParam String plugin,Model model) {
			Plugin p = pluginListService.getByName(plugin);
			if(p.getClassName().equals(PluginConstants.REPORTING)) {
				Reporting reporting = (Reporting) p.getObject();
				String query = "";
				query = reporting.getQuery();
				if(reporting.isFromLoadText()) {
					query = reporting.loadQueryFromTxt();
				};
				model.addAttribute("query", query);
				model.addAttribute("plugin", p);
		   		model.addAttribute("reporting",reporting);
		    	model.addAttribute("forms",((Reporting) p.getObject()).getForms());
			}
			
		    return "pluginview";
	    }
		
}
