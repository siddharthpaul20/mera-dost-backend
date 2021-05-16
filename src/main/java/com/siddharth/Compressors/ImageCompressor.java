package com.siddharth.Compressors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

public class ImageCompressor {
	
	static final Logger mLogger = Logger.getLogger(ImageCompressor.class);

	public static void compressImage(String inputFilePath, String outputFilePath, String imageType) throws IOException
	{
		mLogger.debug("Entering compressImage()");
		File input = new File(inputFilePath);
	    BufferedImage image = ImageIO.read(input);
	
	    File compressedImageFile = new File(outputFilePath);
	    OutputStream os =new FileOutputStream(compressedImageFile);
	
	    Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName(imageType);
	    ImageWriter writer = (ImageWriter) writers.next();
	
	    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	    writer.setOutput(ios);
	
	    ImageWriteParam param = writer.getDefaultWriteParam();
	  
	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    param.setCompressionQuality(0.5f);
	    writer.write(null, new IIOImage(image, null, null), param);

	    os.close();
	    ios.close();
	    writer.dispose();
	    mLogger.debug("Exiting compressImage()");
	}
}
