package com.siddharth.Compressors;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;



public class PdfCompressor extends Compressor {
	
	static final Logger mLogger = Logger.getLogger(PdfCompressor.class);
	
	public PdfCompressor() {
		
	}

	@Override
	public String compressFile(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering compressFile()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, inputType);
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		manipulatePdf(inputFilePath, outputFilePath);
		mLogger.debug("Exiting compressFile()");
		return outputFileName;
	}
	
	/** The resulting PDF file. */
	//public static String RESULT = "results/part4/chapter16/resized_image.pdf";
	/** The multiplication factor for the image. */
	public static float FACTOR = 0.5f;

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * @param src the original PDF
	 * @param dest the resulting PDF
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		mLogger.debug("Entering manipulatePdf()");
	    PdfName key = new PdfName("ITXT_SpecialId");
	    PdfName value = new PdfName("123456789");
	    // Read the file
	    PdfReader reader = new PdfReader(src);
	    int n = reader.getXrefSize();
	    PdfObject object;
	    PRStream stream;
	    // Look for image and manipulate image stream
	    for (int i = 0; i < n; i++) {
	        object = reader.getPdfObject(i);
	        if (object == null || !object.isStream())
	            continue;
	        stream = (PRStream)object;
	       // if (value.equals(stream.get(key))) {
	        PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
	        System.out.println(stream.type());
	        if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
	            PdfImageObject image = new PdfImageObject(stream);
	            BufferedImage bi = image.getBufferedImage();
	            if (bi == null) continue;
	            int width = (int)(bi.getWidth() * FACTOR);
	            int height = (int)(bi.getHeight() * FACTOR);
	            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
	            Graphics2D g = img.createGraphics();
	            g.drawRenderedImage(bi, at);
	            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
	            ImageIO.write(img, "JPG", imgBytes);
	            stream.clear();
	            stream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
	            stream.put(PdfName.TYPE, PdfName.XOBJECT);
	            stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
	            stream.put(key, value);
	            stream.put(PdfName.FILTER, PdfName.DCTDECODE);
	            stream.put(PdfName.WIDTH, new PdfNumber(width));
	            stream.put(PdfName.HEIGHT, new PdfNumber(height));
	            stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
	            stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
	        }
	    }
	    // Save altered PDF
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest), PdfWriter.VERSION_1_5);
//	    stamper.getWriter().setCompressionLevel(9);
//	    int total = reader.getNumberOfPages()+1;
//	    for(int i=1; i<total; i++)
//	    	reader.setPageContent(i, reader.getPageContent(i));
	    stamper.setFullCompression();
        stamper.setFormFlattening(true);
        stamper.setFreeTextFlattening(true);
	    stamper.close();
	    reader.close();
	    mLogger.debug("Exiting manipulatePdf()");
	}
	
}
