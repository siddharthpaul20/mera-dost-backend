package com.siddharth.Convertors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;


public class ImageToPdfConvertor {
	
	static final Logger mLogger = Logger.getLogger(ImageToPdfConvertor.class);
	/**
	 * Converts arbitrary image file to PDF
	 * http://stackoverflow.com/a/42937466/241986
	 * @param imageFile contents of JPEG or PNG file
	 * @param outputStream stream to write out pdf, always closed after this method execution.
	 * @throws IOException when there' an actual exception or image is not valid
	 */
	public static void imageToPdf(String inputFilePath, String outputFilePath) throws IOException {
		mLogger.debug("Entering imageToPdf()");
		byte[] imageFile = FileUtils.readFileToByteArray(new File(inputFilePath));
		OutputStream outputStream = new FileOutputStream(outputFilePath);
		
	    try {
	        Image image;
	        try {
	            image = Image.getInstance(imageFile);
	        } catch (BadElementException bee) {
	        	mLogger.error("Exception occured in imageToPdf while getting instance of image.");
	            throw new IOException(bee);
	        }

	        //See http://stackoverflow.com/questions/1373035/how-do-i-scale-one-rectangle-to-the-maximum-size-possible-within-another-rectang
	        Rectangle A4 = PageSize.A4;

	        float scalePortrait = Math.min(A4.getWidth() / image.getWidth(),
	                A4.getHeight() / image.getHeight());

	        float scaleLandscape = Math.min(A4.getHeight() / image.getWidth(),
	                A4.getWidth() / image.getHeight());

	        // We try to occupy as much space as possible
	        // Sportrait = (w*scalePortrait) * (h*scalePortrait)
	        // Slandscape = (w*scaleLandscape) * (h*scaleLandscape)

	        // therefore the bigger area is where we have bigger scale
	        boolean isLandscape = scaleLandscape > scalePortrait;

	        float w;
	        float h;
	        if (isLandscape) {
	            A4 = A4.rotate();
	            w = image.getWidth() * scaleLandscape;
	            h = image.getHeight() * scaleLandscape;
	        } else {
	            w = image.getWidth() * scalePortrait;
	            h = image.getHeight() * scalePortrait;
	        }

	        Document document = new Document(A4, 10, 10, 10, 10);

	        try {
	            PdfWriter.getInstance(document, outputStream);
	        } catch (DocumentException e) {
	        	mLogger.error("Exception occured in imageToPdf while getting instance of PdfWriter.");
	            throw new IOException(e);
	        }
	        document.open();
	        try {
	            image.scaleAbsolute(w, h);
	            float posH = (A4.getHeight() - h) / 2;
	            float posW = (A4.getWidth() - w) / 2;

	            image.setAbsolutePosition(posW, posH);
	            image.setBorder(Image.NO_BORDER);
	            image.setBorderWidth(0);

	            try {
	                document.newPage();
	                document.add(image);
	            } catch (DocumentException de) {
	                throw new IOException(de);
	            }
	        } finally {
	            document.close();
	        }
	    } finally {
	        outputStream.close();
	    }
	    mLogger.debug("Exiting ImageToPdfConvertor()");
	}

}
