package com.siddharth.Controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siddharth.ServiceManager.ServiceManager;
@RestController
@CrossOrigin(origins = "*")
public class ControllerClass {
	
	static final Logger logger = Logger.getLogger(ControllerClass.class);
	
	ServiceManager mServiceManager = null;
	
	public ControllerClass() {
		intializeClass();
	}
	
	public void intializeClass()
	{
		logger.info("Entering Initializing class of ControllerClass");
		mServiceManager = ServiceManager.getInstance();
		logger.info("Exiting Initializing class of ControllerClass");
	}
	 
	@PostMapping(path = "/convertDocxToPdf",consumes = "multipart/form-data", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> convertDocxToPdf(@RequestParam("inputFile") MultipartFile file,
    		@RequestParam("inputType") String inputType, @RequestParam("outputType") String outputType)
    {
		logger.debug("Entering convertDocxToPdf()");
		ResponseEntity<InputStreamResource> response = null;
		try {
			String fileName = mServiceManager.convertFile(inputType, outputType, file);
			System.out.println(new File(".").getAbsoluteFile());
			ClassPathResource pdfFile = new ClassPathResource("uploads/"+fileName);
//			Path path = Paths.get(fileName);
//	        byte[] data = Files.readAllBytes(path);
//	        ByteArrayResource resource = new ByteArrayResource(data);
//	 
//	        return ResponseEntity.ok()
//	                // Content-Disposition
//	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
//	                // Content-Type
//	                .contentType(MediaType.APPLICATION_OCTET_STREAM) //
//	                // Content-Lengh
//	                .contentLength(data.length) //
//	                .body(resource);
			
			HttpHeaders headers = new HttpHeaders();
			  headers.setContentType(MediaType.parseMediaType("application/pdf"));
			  headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
			  headers.add("Access-Control-Allow-Headers", "Content-Type");
			  headers.add("Access-Control-Expose-Headers", "Content-Disposition");
			  headers.add("Content-Disposition", "filename=" + fileName);
			  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			  headers.add("Pragma", "no-cache");
			  headers.add("Expires", "0");
			 
			  headers.setContentLength(pdfFile.contentLength());
			 response = new ResponseEntity<InputStreamResource>(
			    new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception occured in convertDocxToPdf()",e);
		}
		logger.debug("Exiting convertDocxToPdf()");
		return response;
    }

}
