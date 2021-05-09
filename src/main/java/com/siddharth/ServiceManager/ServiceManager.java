package com.siddharth.ServiceManager;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.aspose.words.Document;
import com.siddharth.Convertors.Convertor;
import com.siddharth.FileStorage.FileStorageService;
import com.siddharth.commons.FileUtility;
import com.siddharth.executor.RequestActionExecutor;

public class ServiceManager {
	
	static final Logger logger = Logger.getLogger(ServiceManager.class);
	
	private RequestActionExecutor mRequestActionExecutor = null;
	private Convertor mConvertor = null;
	private static ServiceManager mServiceManager = null;
	public FileStorageService mFileStorageService = null;
	private ServiceManager() {
		initializeClass();
	}
	
	private void initializeClass()
	{
		logger.info("Entering intialization of ServiceManager");
		mRequestActionExecutor = new RequestActionExecutor();
		mConvertor = new Convertor();
		mFileStorageService = new FileStorageService();
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
		mConvertor.convert(inputType, outputType, pathOfDownloadedFileOnServer);
		// Load the Word document from disk
		Document doc = new Document(getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer));
		doc.save(getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer));
		FileUtility.deleteFile(getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer));
		logger.debug("Exiting convertFile()");
		return getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer);
	}
	
	public String getPathOfDownloadedFileOnServerFromName(String fileName)
	{
		logger.debug("Entering getPathOfDownloadedFileOnServerFromName()");
		logger.debug("Exiting getPathOfDownloadedFileOnServerFromName()");
		return mFileStorageService.getFileStorageLocation()+"\\"+fileName;
	}
	
	public String getPathOfConvertedFileOnServerFromName(String fileName)
	{
		return fileName.substring(0, fileName.lastIndexOf(".")==-1? fileName.length()-1: fileName.lastIndexOf("."))+".pdf";
	}
}