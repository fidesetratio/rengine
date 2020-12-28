package com.app.plugin.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class LoggingItemWriter implements ItemWriter<TaskTable> {
	 private static final Logger logger = LoggerFactory.getLogger(LoggingItemWriter.class);

	@Override
	public void write(List<? extends TaskTable> items) throws Exception {
		// TODO Auto-generated method stub
		for(TaskTable i:items) {
			System.out.println(i.getReg_spaj());
		}
		logger.info("size="+items.size());
	}

}
