package com.siddharth.commons;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;


public class FileUtility {
	
	static final Logger logger = Logger.getLogger(FileUtility.class);
	
	public static void deleteFile(String pathOfFile)
	{
		logger.debug("Entering deleteFile()");
		File file = new File(pathOfFile);
		if(file.delete())
			logger.info("Delete file succussfull : "+pathOfFile);
		else
			logger.info("Delete file failed : "+pathOfFile);
		logger.debug("Exiting deleteFile()");
	}
	
	public static String GetConfigPath(String configName, String applicationName)
	{
		logger.debug("Entering GetConfigPath()");
		String configPath = null;
		
		if(null != applicationName)
		{
			configName = applicationName + "//" + configName;
			configPath = Paths.get(".." , configName).toString();
		}
		else
		{
			configPath = Paths.get(configName).toString();
		}
		logger.debug("Entering GetConfigPath()");
		return configPath;
	}

}
