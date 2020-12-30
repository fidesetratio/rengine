package com.app.plugin.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

public class ExcellWriter implements ItemWriter<TaskTable>,StepExecutionListener {

	    private HSSFWorkbook wb;
	    private WritableResource resource;
	    private int row;
	    private String headers;
	    private String total;
	    private List<String> headersExcel;
	
	    private String keyId;
	    @Override
	    public void beforeStep(StepExecution stepExecution) {
	    		wb = new HSSFWorkbook();
		        HSSFPalette palette = wb.getCustomPalette();
		        HSSFSheet s = wb.createSheet();
		        row = 0;
		        headers = stepExecution.getJobExecution().getJobParameters().getString("headers");
		        headersExcel =  Stream.of(headers.split(";"))
		        	     .map(String::trim)
		        	     .collect(Collectors.toList());
		        total = stepExecution.getJobExecution().getJobParameters().getString("total");
		        keyId = stepExecution.getJobExecution().getJobParameters().getString("keyId");
		        resource = new FileSystemResource(keyId+".xls");
		        System.out.println(resource.getDescription());
		        createTitleRow(s, palette);
		        createHeaderRow(s,headers);
		        System.out.println("selectQuery22:"+headers);
		        System.out.println("total:"+total);
	    	
	    }
	    
	    @Override
		public ExitStatus afterStep(StepExecution stepExecution) {
		
	    	  if (wb == null) {
		            return ExitStatus.FAILED;
		        }
		        createFooterRow();
		        try (BufferedOutputStream bos = new BufferedOutputStream(resource.getOutputStream())) {
		            wb.write(bos);
		            bos.flush();
		            wb.close();
		        } catch (IOException ex) {
		            throw new ItemStreamException("Error writing to output file", ex);
		        }
		        row = 0;
		        
	        return ExitStatus.COMPLETED;
		}
	    

	public void write(List<? extends TaskTable> items) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println(items.size()+"sizekuu");
	    if(wb == null) {
	    	/*	wb = new HSSFWorkbook();
	    		HSSFPalette palette = wb.getCustomPalette();
		        HSSFSheet s = wb.createSheet();

		        resource = new FileSystemResource("output.xlsx");
		        row = 0;
		        createTitleRow(s, palette);
		        createHeaderRow(s,headers);*/
	    }
		HSSFSheet s = wb.getSheetAt(0);
	     
		 for (TaskTable i : items) {
			 Row r = s.createRow(row++);
			 	
	         /*   Row r = s.createRow(row++);
	            Cell c = r.createCell(0);
	            c.setCellValue(i.getReg_spaj());

	            c = r.createCell(1);
	            c.setCellValue(i.getReg_spaj());

	            c = r.createCell(2);
	            c.setCellValue(i.getReg_spaj()); */
			 	int j = 0;
			 	for(int it=1;it<headersExcel.size();it++) {
			 		Cell c = r.createCell(j);
			 		Object o = i.get(headersExcel.get(it));
			 		if(o != null) {
			 		if(o instanceof String) {
			 			c.setCellValue((String)o);
			 		}else if(o instanceof Integer) {
				 		c.setCellValue((Integer)o);
			 		}else if(o instanceof Date) {
				 		c.setCellValue((Date)o);
			 		}else if(o instanceof Double)
			 		{
			 			c.setCellValue((Double)o);
			 		}else {
			 			c.setCellValue(o.toString());
			 		}
			 		
			 		j++;
			 		};
			 	}
			 

	            
	        }
	}
	private void createTitleRow(HSSFSheet s, HSSFPalette palette) {
        HSSFColor redish = palette.findSimilarColor((byte) 0xE6, (byte) 0x50, (byte) 0x32);
        palette.setColorAtIndex(redish.getIndex(), (byte) 0xE6, (byte) 0x50, (byte) 0x32);

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(redish.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        HSSFRow r = s.createRow(row);

        Cell c = r.createCell(0);
        c.setCellValue("Internal Use Only");
        r.createCell(1).setCellStyle(headerStyle);
        r.createCell(2).setCellStyle(headerStyle);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        c.setCellStyle(headerStyle);

        CellUtil.setAlignment(c, HorizontalAlignment.CENTER);

        row++;
    }
	 private void createHeaderRow(HSSFSheet s,String headers) {
		 
		    List<String> list = new ArrayList<String>(Arrays.asList(headers.split(";")));
	        CellStyle cs = wb.createCellStyle();
	        cs.setWrapText(true);
	        cs.setAlignment(HorizontalAlignment.LEFT);

	        HSSFRow r = s.createRow(row);
	        r.setRowStyle(cs);
	        int i = 0;
	        Cell c = null;
	       for(int j = 1;j<list.size();j++) {
	    	    c= r.createCell(i);
	    	    String h = list.get(j);
	        	c.setCellValue(h.toUpperCase());
	        	s.setColumnWidth(i, poiWidth(18.0));
	        	i++;
	       }
	        
	        
			/*
			 * 
			 * 
			 * c.setCellValue("Author"); s.setColumnWidth(0, poiWidth(18.0)); c =
			 * r.createCell(1); c.setCellValue("Book Name"); s.setColumnWidth(1,
			 * poiWidth(24.0)); c = r.createCell(2); c.setCellValue("ISBN");
			 * s.setColumnWidth(2, poiWidth(18.0)); c = r.createCell(3);
			 * c.setCellValue("Price"); s.setColumnWidth(3, poiWidth(18.0));
			 */
	        row++;
	    }
	 private int poiWidth(double width) {
	        return (int) Math.round(width * 256 + 200);
	    }	
	 
	 private void createFooterRow() {
	        //HSSFSheet s = wb.getSheetAt(0);
	        //HSSFRow r = s.createRow(row);
	        //Cell c = r.createCell(3);
	        //c.setCellType(CellType.FORMULA);
	       // c.setCellFormula(String.format("SUM(D3:D%d)", row));
	        row++;

	    }

}
