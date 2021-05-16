package com.siddharth.Compressors;

import org.apache.log4j.Logger;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class PptxCompressor extends Compressor {

static final Logger mLogger = Logger.getLogger(PptxCompressor.class);
	
	public PptxCompressor() {
		
	}

	@Override
	public String compressFile(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering compressFile()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, ".pdf");
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		mLogger.debug("Exiting compressFile()");
		return outputFileName;
	}

}
