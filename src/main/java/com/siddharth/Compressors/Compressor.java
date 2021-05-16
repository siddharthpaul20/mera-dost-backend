package com.siddharth.Compressors;

import org.apache.log4j.Logger;

import com.siddharth.Convertors.Convertor;
import com.siddharth.commons.FileType;
import com.siddharth.commons.exceptions.InvalidFileTypeException;

public class Compressor {
	
	static final Logger mLogger = Logger.getLogger(Compressor.class);
	
	public Compressor()
	{
		initializeClass();
	}
	
	public void initializeClass()
	{
		mLogger.debug("Entering intialization of Compressor");
		mLogger.debug("Exiting intialization of Compressor");
	}

	public Compressor getCompressorObject(String inputType)
	{
		mLogger.debug("Entering getCompressorObject()");
		Compressor compressor = null;
		FileType fileType = FileType.getEnum(inputType);
		if(fileType.equals(FileType.invalid))
		{
			throw new InvalidFileTypeException("Conversion Failed. Invalid file type of input file.");
		}
		if(fileType.equals(FileType.pdf))
			compressor = new PdfCompressor();
		if(fileType.equals(FileType.pptx))
			compressor = new PptxCompressor();
		if(fileType.equals(FileType.ppt))
			compressor = new PptCompressor();
		if(fileType.equals(FileType.png))
			compressor = new PngCompressor();
		if(fileType.equals(FileType.jpeg)||fileType.equals(FileType.jpg))
			compressor = new JpegCompressor();
		mLogger.debug("Exiting getCompressorObject()");
		return compressor;
	}
	
	public String compressFile(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception
	{
		Compressor compressor = getCompressorObject(inputType);
		return compressor.compressFile(inputType, pathOfDownloadedFileOnServer, uploadDirectoryPath);
	}
}
