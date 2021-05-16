package com.siddharth.Convertors;

import org.apache.log4j.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class PptxToPdfConvertor extends Convertor {
	
	static final Logger mLogger = Logger.getLogger(PptxToPdfConvertor.class);

	public PptxToPdfConvertor() {
		
	}
	
	@Override
	public String convertToPdf(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering convertToPdf()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, ".pdf");
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		if(inputFilePath.endsWith(".ppt"))
			convertPPTToPDF(inputFilePath, outputFilePath, ".ppt");
		else
			convertPPTToPDF(inputFilePath, outputFilePath, ".pptx");
		
		mLogger.debug("Exiting convertToPdf()");
		return outputFileName;
	}
	
	public void convertPPTToPDF(String inputFilePath, String outputFilePath, String fileType) throws Exception
	{
		mLogger.debug("Entering convertPPTToPDF()");
	    FileInputStream inputStream = new FileInputStream(inputFilePath);
	    double zoom = 2;
	    AffineTransform at = new AffineTransform();
	    at.setToScale(zoom, zoom);
	    Document pdfDocument = new Document();
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(outputFilePath));
	    PdfPTable table = new PdfPTable(1);
	    pdfWriter.open();
	    pdfDocument.open();
	    Dimension pgsize = null;
	    Image slideImage = null;
	    BufferedImage img = null;
	    if (fileType.equalsIgnoreCase(".ppt")) {
	    	HSLFSlideShow ppt = new HSLFSlideShow(inputStream);
	        pgsize = ppt.getPageSize();
	        List<HSLFSlide> slide = ppt.getSlides();
	        pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
	        pdfWriter.open();
	        pdfDocument.open();
	        for (int i = 0; i < slide.size(); i++) {
	            img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
	            Graphics2D graphics = img.createGraphics();
	            graphics.setTransform(at);

	            graphics.setPaint(Color.white);
	            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	            slide.get(i).draw(graphics);
	            graphics.getPaint();
	            slideImage = Image.getInstance(img, null);
	            table.addCell(new PdfPCell(slideImage, true));
	        }
	        ppt.close();
	    }
	    if (fileType.equalsIgnoreCase(".pptx")) {
	        XMLSlideShow ppt = new XMLSlideShow(inputStream);
	        pgsize = ppt.getPageSize();
	        List<XSLFSlide> slide = ppt.getSlides();
	        pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
	        pdfWriter.open();
	        pdfDocument.open();
	        for (int i = 0; i < slide.size(); i++) {
	            img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
	            Graphics2D graphics = img.createGraphics();
	            graphics.setTransform(at);

	            graphics.setPaint(Color.white);
	            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	            slide.get(i).draw(graphics);
	            graphics.getPaint();
	            slideImage = Image.getInstance(img, null);
	            table.addCell(new PdfPCell(slideImage, true));
	        }
	        ppt.close();
	    }
	    pdfDocument.add(table);
	    pdfDocument.close();
	    pdfWriter.close();
	    mLogger.debug("Exiting convertPPTToPDF()");
	}
}
