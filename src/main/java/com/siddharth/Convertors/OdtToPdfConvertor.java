package com.siddharth.Convertors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.odftoolkit.odfdom.converter.pdf.PdfConverter;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class OdtToPdfConvertor extends Convertor {

static final Logger mLogger = Logger.getLogger(OdtToPdfConvertor.class);
	
	public OdtToPdfConvertor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String convertToPdf(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering convertToPdf()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, ".pdf");
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		convertOdtToPdf(inputFilePath, outputFilePath);
		
		mLogger.debug("Exiting convertToPdf()");
		return outputFileName;
	}
	
	public void convertOdtToPdf(String inputPath, String outputPath) throws Exception
	{
		mLogger.debug("Entering convertOdtToPdf()");
		InputStream inStream = new FileInputStream(new File(inputPath));
		OdfTextDocument document = OdfTextDocument.loadDocument(inStream);
		PdfOptions options = PdfOptions.create();
		
		OutputStream outStream = new FileOutputStream(new File(outputPath));
		PdfConverter.getInstance().convert(document, outStream, options);
		
		mLogger.debug("Exiting convertOdtToPdf()");
	}
}
