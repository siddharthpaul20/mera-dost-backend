package com.siddharth.executor;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.siddharth.ServiceManager.ServiceManager;

public class RequestActionExecutor {

	static final Logger logger = Logger.getLogger(RequestActionExecutor.class);

	public String downloadFileToServer(MultipartFile file)
	{
		logger.debug("Entering downloadFileToServer()");
		String downloadedPath;
		ServiceManager serviceManager = ServiceManager.getInstance();
		downloadedPath = serviceManager.mFileStorageService.storeFile(file,file.getOriginalFilename());
		logger.debug("Exiting downloadFileToServer()");
		return downloadedPath;
	}
}
