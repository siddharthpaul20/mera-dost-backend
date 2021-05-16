package com.siddharth.Compressors;

import org.apache.log4j.Logger;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class PngCompressor extends Compressor {

	static final Logger mLogger = Logger.getLogger(PngCompressor.class);
	
	public PngCompressor() {
		
	}

	@Override
	public String compressFile(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering compressFile()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, inputType);
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		ImageCompressor.compressImage(inputFilePath, outputFilePath, inputType.replace(".", ""));
		mLogger.debug("Exiting compressFile()");
		return outputFileName;
	}
}
