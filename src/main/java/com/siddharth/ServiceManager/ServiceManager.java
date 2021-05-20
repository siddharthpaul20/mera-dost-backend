package com.siddharth.ServiceManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siddharth.Compressors.Compressor;
import com.siddharth.Convertors.Convertor;
import com.siddharth.FileStorage.FileStorageService;
import com.siddharth.commons.Constants;
import com.siddharth.commons.FileUtility;
import com.siddharth.commons.PropertyReader;
import com.siddharth.executor.RequestActionExecutor;

public class ServiceManager {
	
	static final Logger logger = Logger.getLogger(ServiceManager.class);
	
	private RequestActionExecutor mRequestActionExecutor = null;
	private Convertor mConvertor = null;
	private Compressor mCompressor = null;
	private static ServiceManager mServiceManager = null;
	public FileStorageService mFileStorageService = null;
	private PropertyReader mpropReader = null;
	private ServiceManager() {
		initializeClass();
	}
	
	private void initializeClass()
	{
		logger.info("Entering intialization of ServiceManager");
		mRequestActionExecutor = new RequestActionExecutor();
		mConvertor = new Convertor();
		mCompressor = new Compressor();
		mFileStorageService = new FileStorageService();
		mpropReader = new PropertyReader();
		logger.info("Exiting intialization of ServiceManager");
	}
	
	public static ServiceManager getInstance()
	{
		if(mServiceManager == null)
		{
			mServiceManager = new ServiceManager();
		}
		return mServiceManager;
	}
	
	public String convertFile(String inputType, String outputType, MultipartFile file) throws Exception
	{
		logger.debug("Entering convertFile()");
		String pathOfDownloadedFileOnServer = mRequestActionExecutor.downloadFileToServer(file);
		String uploadDirectoryPath = mpropReader.getProperty(Constants.UPLOAD_DIRECTORY_PROPERTY);
		String outputFileName = mConvertor.convert(inputType, outputType, pathOfDownloadedFileOnServer, uploadDirectoryPath);
		FileUtility.deleteFile(getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer));
		logger.debug("Exiting convertFile()");
		return outputFileName;
	}
	
	public String compressFile(String inputType, MultipartFile file) throws Exception
	{
		logger.debug("Entering compressFile()");
		String pathOfDownloadedFileOnServer = mRequestActionExecutor.downloadFileToServer(file);
		String uploadDirectoryPath = mpropReader.getProperty(Constants.UPLOAD_DIRECTORY_PROPERTY);
		String outputFileName = mCompressor.compressFile(inputType, pathOfDownloadedFileOnServer, uploadDirectoryPath);
		FileUtility.deleteFile(getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer));
		logger.debug("Exiting compressFile()");
		return outputFileName;
	}
	
	public String getPathOfDownloadedFileOnServerFromName(String fileName)
	{
		logger.debug("Entering getPathOfDownloadedFileOnServerFromName()");
		logger.debug("Exiting getPathOfDownloadedFileOnServerFromName()");
		return mFileStorageService.getFileStorageLocation()+"/"+fileName;
	}
	
	public String getPathOfConvertedFileOnServerFromName(String fileName, String outputType)
	{
		return fileName.substring(0, fileName.lastIndexOf(".")==-1? fileName.length()-1: fileName.lastIndexOf("."))+outputType;
	}
	
	public ResponseEntity<InputStreamResource> getResponseObjectToSendFile(String fileName, String mediaType) throws IOException
	{
		logger.debug("Entering getResponseObjectToSendFile()");
		ResponseEntity<InputStreamResource> response = null;
//		ClassPathResource pdfFile = new ClassPathResource("uploads/"+fileName);
		File pdfFile = new File(mpropReader.getProperty(Constants.UPLOAD_DIRECTORY_PROPERTY)+Constants.PATH_SEPERATOR+fileName);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(mediaType));
		headers.add(Constants.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT");
		headers.add(Constants.ACCESS_CONTROL_ALLOW_HEADERS, Constants.CONTENT_TYPE);
		headers.add(Constants.ACCESS_CONTROL_EXPOSE_HEADERS, Constants.CONTENT_DISPOSITION);
		headers.add(Constants.CONTENT_DISPOSITION, Constants.FILENAME_EQUAL + fileName);
		headers.add(Constants.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		headers.add(Constants.PRAGMA, "no-cache");
		headers.add(Constants.EXPIRES, "0");
		 
		//headers.setContentLength(pdfFile.contentLength());
		headers.setContentLength((int) pdfFile.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(pdfFile));
//		response = new ResponseEntity<InputStreamResource>(
//		    new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
		response = new ResponseEntity<InputStreamResource>(
				new InputStreamResource(inputStream), headers, HttpStatus.OK);
		logger.debug("Exiting getResponseObjectToSendFile()");
		return response;
	}
}