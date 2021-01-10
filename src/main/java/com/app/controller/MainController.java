package com.app.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.interceptor.Layout;
import com.app.plugin.constants.PluginConstants;
import com.app.plugin.core.TaskPluginMonitoring;
import com.app.plugin.core.model.Plugin;
import com.app.plugin.core.model.Reporting;
import com.app.plugin.core.model.TaskMonitoring;
import com.app.services.PluginListServices;
import com.app.services.QueryServices;

@Controller
public class MainController {
	@Autowired
	private PluginListServices pluginListService;
	  
	@Autowired
	private QueryServices queryServices;
	
	
	
	@Autowired
	private TaskPluginMonitoring taskPluginMonitoring;
	
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
				reporting.loadReportQueryFromTxt();
				
				String multiq = StringUtils.join(reporting.getMultiq(), ";");
				String multir = StringUtils.join(reporting.getMultir(), ";");
				
				String reportQuery = reporting.getReportselectQuery()+" from "+ reporting.getReportfromQuery() + " "+ reporting.getReportwhereQuery();
				model.addAttribute("query", query);
				model.addAttribute("reportQuery",reportQuery);
				model.addAttribute("plugin", p);
				model.addAttribute("multiq", multiq);
				model.addAttribute("multir", multir);
				model.addAttribute("mulitQlist",reporting.getMultiq());
				model.addAttribute("mulitRlist",reporting.getMultir());
				
				
		   		model.addAttribute("reporting",reporting);
		   		int manyForm = ((Reporting) p.getObject()).getForms().size();
		   		
		   		BigDecimal sisa =new BigDecimal(manyForm/3);
		   		sisa = sisa.setScale(BigDecimal.ROUND_CEILING);
		   		System.out.println(sisa.setScale(BigDecimal.ROUND_CEILING));
		   		model.addAttribute("manyformQ",manyForm);
		   		model.addAttribute("manyRowQ",sisa.toBigInteger().intValue());
		   		
		    	
		   		
		    	model.addAttribute("forms",((Reporting) p.getObject()).getForms());
		    	model.addAttribute("reportforms",((Reporting) p.getObject()).getReportingform());

			}
			
		    return "pluginview";
	    }
		
		@GetMapping("/statustask")
	    public String statustask(Model model) {
			
			List<TaskMonitoring> lists = taskPluginMonitoring.list();
			System.out.println(lists.size());
			Collections.sort(lists, new Comparator<TaskMonitoring>() {
			    public int compare(TaskMonitoring o1, TaskMonitoring o2) {
			    	 if (o1.getDateTime() == null || o2.getDateTime() == null)
			    	        return 0;
			    	      return o2.getDateTime().compareTo(o1.getDateTime());
			    }
			});
			
			model.addAttribute("lists", lists);
			
			 return "statustask";
		}
		
		@Layout("layouts/blank")
		@GetMapping("/statustaskf")
	    public String fragmentstatustask(Model model) {
			
			List<TaskMonitoring> lists = taskPluginMonitoring.list();
			System.out.println(lists.size());
			Collections.sort(lists, new Comparator<TaskMonitoring>() {
			    public int compare(TaskMonitoring o1, TaskMonitoring o2) {
			    	 if (o1.getDateTime() == null || o2.getDateTime() == null)
			    	        return 0;
			    	      return o2.getDateTime().compareTo(o1.getDateTime());
			    }
			});
			
			model.addAttribute("lists", lists);
			
			 return "statustaskf";
		}
}
