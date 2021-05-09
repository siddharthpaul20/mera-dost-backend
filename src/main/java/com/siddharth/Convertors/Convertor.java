package com.siddharth.Convertors;

import org.apache.log4j.Logger;

import com.siddharth.Controller.ControllerClass;
import com.siddharth.commons.FileType;
import com.siddharth.commons.exceptions.InvalidFileTypeException;

public class Convertor {
	
	private Convertor mPdfConvertor = null;
	private Convertor mDocxConvertor = null;
	static final Logger logger = Logger.getLogger(ControllerClass.class);
	
	public Convertor()
	{
		initializeClass();
	}
	
	public void initializeClass()
	{
		logger.debug("Entering intialization of Convertor");
		logger.debug("Exiting intialization of Convertor");
	}
	
	
	public void convert(String inputType, String outputType, String pathOfDownloadedFileOnServer)
	{
		logger.debug("Entering convert()");
		FileType outputFileType = FileType.getEnum(outputType);
		if(outputFileType.equals(FileType.invalid))
		{
			throw new InvalidFileTypeException("Conversion Failed. Invalid file type of output file.");
		}
		if(outputFileType.equals(FileType.pdf))
			convertToPdf(inputType, pathOfDownloadedFileOnServer);
		if(outputFileType.equals(FileType.docx))
			convertToDocx(inputType, pathOfDownloadedFileOnServer);
		logger.debug("Exiting convert()");
	}
	public void convertToPdf(String inputType, String pathOfDownloadedFileOnServer)
	{
		Convertor convertor = getConvertorObject(inputType);
		convertor.convertToPdf(null, pathOfDownloadedFileOnServer);
	}
	public void convertToDocx(String inputType, String pathOfDownloadedFileOnServer)
	{
		Convertor convertor = getConvertorObject(inputType);
		convertor.convertToDocx(null, pathOfDownloadedFileOnServer);
	}
	
	public Convertor getConvertorObject(String inputType)
	{
		logger.debug("Entering getConvertorObject()");
		Convertor convertor = null;
		FileType fileType = FileType.getEnum(inputType);
		if(fileType.equals(FileType.invalid))
		{
			throw new InvalidFileTypeException("Conversion Failed. Invalid file type of input file.");
		}
		if(fileType.equals(FileType.pdf))
			convertor = new PdfToDocxConvertor();
		if(fileType.equals(FileType.docx))
			convertor = new DocxToPdfConvertor();
		logger.debug("Exiting getConvertorObject()");
		return convertor;
	}
}
