package com.app.controller;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.QueryModel;
import com.app.model.Table;
import com.app.plugin.core.TaskPluginMonitoring;
import com.app.plugin.core.TaskTable;
import com.app.plugin.core.model.ReportingQuery;
import com.app.plugin.core.model.ReportingQueryResponse;
import com.app.plugin.core.model.TaskMonitoring;
import com.app.services.QueryServices;
import com.app.services.TaskPluginServices;

@RestController
@RequestMapping("/plugin")
public class PluginRest {
	
	@Autowired
	private QueryServices queryServices;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private TaskPluginServices taskPluginServices;
	
	
	@Autowired
	private TaskPluginMonitoring taskPluginMonitoring;

	@PostMapping(path = "/query", consumes = "application/json", produces = "application/json")
	public Table query(@RequestBody QueryModel qmodel) {
	    Table table = new Table();
		String query = qmodel.getQuery();
		table = queryServices.query(query);
		return table;
		
	}
	
	@RequestMapping("/download/{file:.+}")
	public void download(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("file") String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}

	@RequestMapping("/remove/{keyId:.+}")
	public void remove(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("keyId") String keyId) throws IOException {
			taskPluginMonitoring.delete(keyId);
	}





	
	@PostMapping(path = "/executeTaskPost", consumes = "application/json", produces = "application/json")
	public ReportingQueryResponse executeTaskPost(@RequestBody ReportingQuery reportingModel) {
		String keyId = TaskPluginMonitoring.keyIdGenerator();
		String pluginName = reportingModel.getPluginName();
	    JobParametersBuilder paramsBuilder = new JobParametersBuilder();
	    
	    System.out.println("squery:"+reportingModel.getReportingSelectQuery());
	    System.out.println("fquery:"+reportingModel.getReportingFromQuery());
	    System.out.println("wquery:"+reportingModel.getReportingWhereQuery());
	    paramsBuilder.addString("selectQuery",reportingModel.getReportingSelectQuery());
	    paramsBuilder.addString("fromQuery",reportingModel.getReportingFromQuery());
	    paramsBuilder.addString("whereQuery",reportingModel.getReportingWhereQuery());
	    paramsBuilder.addLong("time",System.currentTimeMillis());
        paramsBuilder.addString("keyId",keyId);	      
        taskPluginMonitoring.put(new TaskMonitoring(keyId,pluginName));
    	taskPluginServices.executeTask2(paramsBuilder, keyId, taskPluginMonitoring);
    	ReportingQueryResponse response = new ReportingQueryResponse();
    	response.setKeyId(keyId);
    	response.setPluginName(pluginName);
    	return response;
	}
	@GetMapping("/executeTask")
    public String executeTask() {
			String keyId = TaskPluginMonitoring.keyIdGenerator();
		    JobParametersBuilder paramsBuilder = new JobParametersBuilder();
	    //    paramsBuilder.addString("selectQuery", "select rownum as number1,a.reg_spaj");
	     //   paramsBuilder.addString("fromQuery", "FROM eka.mst_policy a");
	      //  paramsBuilder.addString("whereQuery", "where 1=1 and rownum=1");	     
		    paramsBuilder.addString("selectQuery", "select rownum as number1,a.mspo_policy_no as no_polis,a.reg_spaj, "
		    		+ " EKA.UTILS.PEMEGANG (a.reg_Spaj) AS pemegang, "
		    		+ "         EKA.UTILS.TERTANGGUNG (a.reg_Spaj) AS tertanggung, "
		    		+ "          EKA.UTILS.TGL_LAHIR_TERTANGGUNG(a.reg_spaj) as tgl_lahir_tertanggung, "
		    		+ "          EKA.UTILS.STATUS_POLIS(a.lssp_id) as status_polis, "
		    		+ "          b.mspr_tsi as up, "
		    		+ "          a.mspo_beg_date as beg_date, "
		    		+ "          c.lsdbs_name as nama_produk, "
		    		+ "          H.NAMA_DIST AS channel," 
		    		+ "          A.mspo_input_date as input_date, "
		    		+ "           k.wilayah, k.cabang_bank");
		    paramsBuilder.addString("fromQuery","from eka.mst_policy a, eka.mst_product_insured b,eka.lst_det_bisnis c, "
		    		+ "eka.lst_cabang e, "
		    		+ "eka.lst_jalur_dist h, "
		    		+ " dms.mst_depository_production@dwhdb k "
		    		+ " ");
		    
		    paramsBuilder.addString("whereQuery","where a.reg_spaj = b.reg_spaj and b.lsbs_id < 300 "
		    		+ " and k.reg_spaj = a.reg_spaj "
		    		+ " and k.prod_ke = 1 "
		    		+ " AND a.lca_id = e.lca_id "
		    		+ " AND e.jalurdis = h.id_dist "
		    		+ "and trunc(A.mspo_input_date) between '01 Jan 2017' and '13 Dec 2020' "
		    		+ "and b.lsbs_id in ('212','223')"
		    		+ "and a.lssp_id = 1 "
		    		+ "and b.lsbs_id = c.lsbs_id and b.lsdbs_number = c.lsdbs_number "
		    		+ " AND a.lspd_id <> 95 "
		    		+ ""
		    		);
	        paramsBuilder.addLong("time",System.currentTimeMillis());
	        paramsBuilder.addString("keyId",keyId);	       
	        taskPluginMonitoring.put(new TaskMonitoring(keyId));
			//taskPluginServices.executeTask(paramsBuilder,keyId);
	        taskPluginServices.executeTask2(paramsBuilder, keyId, taskPluginMonitoring);
		    
	        return keyId;
		
	}
	
	@GetMapping("/taskProgress")
    public TaskMonitoring taskProgress(@RequestParam("taskId")String taskId) {
		    return taskPluginMonitoring.get(taskId);
		
	}
	
	
	@GetMapping("/executeTask2")
    public String executeTask2() {
		 	/*
			 * ItemReader reader = (ItemReader)context.getBean("pagingItemReaderBean");
			 * ItemWriter writer = (ItemWriter)context.getBean("pagingItemWriterBean");
			 */
		  ItemReader reader = (ItemReader)context.getBean("pagingItemReaderBean");
		  ItemWriter writer = (ItemWriter)context.getBean("pagingItemWriterBean");
		  
		  Step step = stepBuilderFactory.get("orderStep1").<TaskTable, TaskTable> chunk(100).reader(reader).writer(writer).build();
		  Flow oneFlowOnly = new FlowBuilder<Flow>("singleFlow").
					start(step).
					build();
		  Job job = jobBuilderFactory
	                .get("SimpleJob")
	                .incrementer(new RunIdIncrementer())
	                .start(oneFlowOnly)
	                .end()
	                .build();

	        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
	        paramsBuilder.addString("selectQuery", "select rownum as number1,a.reg_spaj");
	        paramsBuilder.addString("fromQuery", "FROM eka.mst_policy a");
	        paramsBuilder.addString("whereQuery", "where 1=1");
	        
	        
	        try {
				JobExecution execution = jobLauncher.run(job,paramsBuilder.toJobParameters());
			} catch (JobExecutionAlreadyRunningException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobRestartException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
			/*
			 * Step step = stepBuilderFactory.get("orderStep1").<String, String> chunk(1)
			 * .reader(reader1(null)) .writer(writer1(null)).build();
			 */
	    return "executeTask";
    }
}
