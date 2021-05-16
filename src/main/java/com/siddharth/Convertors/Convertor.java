package com.siddharth.Convertors;

import org.apache.log4j.Logger;

import com.siddharth.Controller.ControllerClass;
import com.siddharth.commons.FileType;
import com.siddharth.commons.exceptions.InvalidFileTypeException;

public class Convertor {
	
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
	
	
	public String convert(String inputType, String outputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception
	{
		logger.debug("Entering convert()");
		String outputFileName = null;
		FileType outputFileType = FileType.getEnum(outputType);
		if(outputFileType.equals(FileType.invalid))
		{
			throw new InvalidFileTypeException("Conversion Failed. Invalid file type of output file.");
		}
		if(outputFileType.equals(FileType.pdf))
			outputFileName = convertToPdf(inputType, pathOfDownloadedFileOnServer, uploadDirectoryPath);
		if(outputFileType.equals(FileType.docx))
			outputFileName = convertToDocx(inputType, pathOfDownloadedFileOnServer, uploadDirectoryPath);
		logger.debug("Exiting convert()");
		return outputFileName;
	}
	public String convertToPdf(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception
	{
		Convertor convertor = getConvertorObject(inputType);
		return convertor.convertToPdf(null, pathOfDownloadedFileOnServer, uploadDirectoryPath);
	}
	public String convertToDocx(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath)
	{
		Convertor convertor = getConvertorObject(inputType);
		return convertor.convertToDocx(null, pathOfDownloadedFileOnServer, uploadDirectoryPath);
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
		if(fileType.equals(FileType.docx)||fileType.equals(FileType.doc))
			convertor = new DocxToPdfConvertor();
		if(fileType.equals(FileType.pptx)||fileType.equals(FileType.ppt))
			convertor = new PptxToPdfConvertor();
		if(fileType.equals(FileType.odt))
			convertor = new OdtToPdfConvertor();
		if(fileType.equals(FileType.png))
			convertor = new PngToPdfConvertor();
		if(fileType.equals(FileType.jpeg)||fileType.equals(FileType.jpg))
			convertor = new JpegToPdfConvertor();
		logger.debug("Exiting getConvertorObject()");
		return convertor;
	}
}
