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
import org.springframework.web.bind.annotation.GetMapping;
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
	 
	@PostMapping(path = "/convertDocument",consumes = "multipart/form-data", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> convertDocxToPdf(@RequestParam("inputFile") MultipartFile file,
    		@RequestParam("inputType") String inputType, @RequestParam("outputType") String outputType)
    {
		logger.debug("Entering convertDocument()");
		ResponseEntity<InputStreamResource> response = null;
		try {
			String fileName = mServiceManager.convertFile(inputType, outputType, file);
			response = mServiceManager.getResponseObjectToSendFile(fileName, MediaType.APPLICATION_PDF_VALUE);
		} catch (Exception e) {
			logger.error("Exception occured in convertDocument()",e);
		}
		logger.debug("Exiting convertDocument()");
		return response;
    }
	
	@PostMapping(path = "/compressDocument",consumes = "multipart/form-data", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> compressDocument(@RequestParam("inputFile") MultipartFile file,
    		@RequestParam("inputType") String inputType)
    {
		logger.debug("Entering compressDocument()");
		ResponseEntity<InputStreamResource> response = null;
		try {
			String fileName = mServiceManager.compressFile(inputType, file);
			response = mServiceManager.getResponseObjectToSendFile(fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		} catch (Exception e) {
			logger.error("Exception occured in compressDocument()",e);
		}
		logger.debug("Exiting compressDocument()");
		return response;
    }
	
	@GetMapping(path = "/")
	public ResponseEntity<String> serverStartedMessage()
	{
		logger.debug("Entering serverStartedMessage()");
		ResponseEntity<String> response = new ResponseEntity<>("Mera dost backend is active.", 
				   HttpStatus.OK);
		logger.debug("Exiting serverStartedMessage()");
		return response;
	}

}
