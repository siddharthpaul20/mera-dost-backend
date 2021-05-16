package com.siddharth.Convertors;

import org.apache.log4j.Logger;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class JpegToPdfConvertor extends Convertor{

	static final Logger mLogger = Logger.getLogger(JpegToPdfConvertor.class);
	
	public JpegToPdfConvertor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String convertToPdf(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering convertToPdf()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, ".pdf");
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		ImageToPdfConvertor.imageToPdf(inputFilePath, outputFilePath);
		mLogger.debug("Exiting convertToPdf()");
		return outputFileName;
	}
	
	
}
