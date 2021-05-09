package com.siddharth.commons;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

public class PropertyReader {
	
	private static final Logger logger = Logger.getLogger(PropertyReader.class);
	private static Properties prop = null;
	
	public PropertyReader()
	{
		if(null == prop)
		{
			prop = new Properties();
			try {
				String configFile = FileUtility.GetConfigPath(Constants.MERA_DOST_APP_CONFIG_FILE_PATH, null);
				InputStream oInputStream = new FileInputStream(configFile);
				prop.load(oInputStream);
			} catch (Exception e) {
				logger.error("Failed to load configuration for the application. Exception: ", e);
			}
		}
	}
	
	public String getProperty(String strKey) {
		return prop.getProperty(strKey);
	}

}
