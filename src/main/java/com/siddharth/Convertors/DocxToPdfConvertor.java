package com.siddharth.Convertors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.Doc;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.w3c.tidy.Tidy;

import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.siddharth.ServiceManager.ServiceManager;
import com.siddharth.commons.Constants;

public class DocxToPdfConvertor extends Convertor {
	
	static final Logger mLogger = Logger.getLogger(DocxToPdfConvertor.class);
	
	public DocxToPdfConvertor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String convertToPdf(String inputType, String pathOfDownloadedFileOnServer, String uploadDirectoryPath) throws Exception {
		mLogger.debug("Entering convertToPdf()");
		ServiceManager serviceManager = ServiceManager.getInstance();
		String inputFilePath = serviceManager.getPathOfDownloadedFileOnServerFromName(pathOfDownloadedFileOnServer);
		String outputFileName = serviceManager.getPathOfConvertedFileOnServerFromName(pathOfDownloadedFileOnServer, ".pdf");
		String outputFilePath = uploadDirectoryPath+Constants.PATH_SEPERATOR+outputFileName;
		
//		convertDocToHTMLUsingApachePOI(inputFilePath, outputFilePath);
		
		if(inputFilePath.endsWith(".docx"))
				convertDocxToPdfUsingDocx4j(inputFilePath, outputFilePath);
		else
			convertDocToPdfUsingDocx4j(inputFilePath, outputFilePath);
		mLogger.debug("Exiting convertToPdf()");
		return outputFileName;
	}
	
	public void convertDocDocxToPdfUsingApachePOI(String inputFilePath, String outputFilePath)
	{
		mLogger.debug("Entering convertDocDocxToPdfUsingApachePOI()");
		String readText=null;
        OutputStream fileForPdf =null;
        try {
	            if(inputFilePath.endsWith(".doc"))
	            {
		            HWPFDocument doc = new HWPFDocument(new FileInputStream(inputFilePath));
		            WordExtractor we=new WordExtractor(doc);
		            readText = we.getText();
		            fileForPdf = new FileOutputStream(new File(outputFilePath)); 
		            we.close();
	            }
	            else if(inputFilePath.endsWith(".docx"))
	            {
	                XWPFDocument docx = new XWPFDocument(new FileInputStream(inputFilePath));
	                // using XWPFWordExtractor Class
	                XWPFWordExtractor we = new XWPFWordExtractor(docx);
	                 readText = we.getText();
	                 fileForPdf = new FileOutputStream(new File(outputFilePath));    
	                 we.close();
	            }
	            Document document = new Document();
	            PdfWriter.getInstance(document, fileForPdf);
	            document.open();
	            document.add(new Paragraph(readText));
	            document.close();
	            fileForPdf.close();
        } catch (Exception e) {
            mLogger.error("Excepting occured in method convertDocDocxToPdfUsingApachePOI(). Exception: ",e);
        }
		mLogger.debug("Exiting convertDocDocxToPdfUsingApachePOI()");
	}
	
	public void convertDocxToPdfUsingDocx4j(String inputFilePath, String outputFilePath)
	{
		mLogger.debug("Exiting convertDocxToPdfUsingDocx4j()");
		try {
            InputStream templateInputStream = new FileInputStream(inputFilePath);
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);

            FileOutputStream os = new FileOutputStream(outputFilePath);
            Docx4J.toPDF(wordMLPackage,os);
            os.flush();
            os.close();
            templateInputStream.close();
        } catch (Throwable e) {
        	mLogger.error("Excepting occured in method convertDocxToPdfUsingDocx4j(). Exception: ",e);
        } 
		mLogger.debug("Exiting convertDocxToPdfUsingDocx4j()");
	}
	
	public void convertDocToHTMLUsingApachePOI(String inputFilePath, String outputFilePath)
	{
		mLogger.debug("Exiting convertDocToHTMLUsingApachePOI()");
		try {
			HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(new FileInputStream(inputFilePath));
			 WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
			     DocumentBuilderFactory.newInstance().newDocumentBuilder()
			         .newDocument());
//			 wordToHtmlConverter.processDocument(wordDocument);
//			 org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
//			 ByteArrayOutputStream out = new ByteArrayOutputStream();
//			 DOMSource domSource = new DOMSource(htmlDocument);
//			 StreamResult streamResult = new StreamResult(out);
//			 TransformerFactory tf = TransformerFactory.newInstance();
//			 Transformer serializer = tf.newTransformer();
//			 serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			 serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//			 serializer.setOutputProperty(OutputKeys.METHOD, "html");
//			 serializer.transform(domSource, streamResult);
//			 out.close();
//			 String result = new String(out.toByteArray());
//			 System.out.println(result);
//			 
			 
			 
//			 HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(is);
//
//	            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
//	                    DocumentBuilderFactory.newInstance().newDocumentBuilder()
//	                            .newDocument());
	            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
					
					@Override
					public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
							float heightInches) {
						 File imageFile = new File("pages/imgs", suggestedName);
		                    imageFile.getParentFile().mkdirs();
		                    InputStream in = null;
		                    FileOutputStream out = null;

		                    try {
		                        in = new ByteArrayInputStream(content);
		                        out = new FileOutputStream(imageFile);
		                        IOUtils.copy(in, out);

		                    } catch (FileNotFoundException e) {
		                        e.printStackTrace();
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    } finally {
		                        if (in != null) {
		                            IOUtils.closeQuietly(in);
		                        }

		                        if (out != null) {
		                            IOUtils.closeQuietly(out);
		                        }

		                    }
		                    return "imgs/" + imageFile.getName();
					}
	            });
	            wordToHtmlConverter.processDocument(wordDocument);
	            org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            DOMSource domSource = new DOMSource(htmlDocument);
	            StreamResult streamResult = new StreamResult(out);


	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty(OutputKeys.METHOD, "html");
	            transformer.transform(domSource, streamResult);
	            out.close();

	            String result = new String(out.toByteArray());
	            System.out.println(result);
	            FileOutputStream fos = new FileOutputStream("pages/abc.html");
	            fos.write(out.toByteArray());
	            fos.close();
	            
	            
	            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	            com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
	            document.open();
	            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
	            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	            htmlContext.setImageProvider(new AbstractImageProvider() {
					
					@Override
					public String getImageRootPath() {
						// TODO Auto-generated method stub
						 return "E:\\Projects\\STS workspace\\MeraDost\\pages";
					}
				});
	            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
	              Pipeline<?> pipeline =
	                  new CssResolverPipeline(cssResolver,
	                          new HtmlPipeline(htmlContext,
	                              new PdfWriterPipeline(document, writer)));
	              XMLWorker worker = new XMLWorker(pipeline, true);
	              XMLParser p = new XMLParser(worker);
	              String xhtmlOutputFile = getXHTMLFromHTML("pages/abc.html", "pages/def.xhtml");
	              p.parse(new FileInputStream(xhtmlOutputFile));
	              document.close();
	              System.out.println("Done.");        
			 
			 
        } catch (Throwable e) {
        	mLogger.error("Excepting occured in method convertDocToHTMLUsingApachePOI(). Exception: ",e);
        } 
		mLogger.debug("Exiting convertDocToHTMLUsingApachePOI()");
	}
	
	public void convertDocToPdfUsingDocx4j(String inputFilePath, String outputFilePath)
	{
		mLogger.debug("Entering convertDocToPdfUsingDocx4j()");
		try {
            InputStream templateInputStream = new FileInputStream(inputFilePath);
            //WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
            WordprocessingMLPackage mlPackage = Doc.convert(templateInputStream);

            FileOutputStream os = new FileOutputStream(outputFilePath);
            Docx4J.toPDF(mlPackage,os);
            os.flush();
            os.close();
            templateInputStream.close();
        } catch (Throwable e) {
        	mLogger.error("Excepting occured in method convertDocxToPdfUsingDocx4j(). Exception: ",e);
        } 
		mLogger.debug("Exiting convertDocToPdfUsingDocx4j()");
	}
	
	public String getXHTMLFromHTML(String inputFile, String outputFile) throws Exception {
		mLogger.debug("Entering getXHTMLFromHTML()");
        File file = new File(inputFile);
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(outputFile);
            is = new FileInputStream(file);
            Tidy tidy = new Tidy(); 
            tidy.setXHTML(true); 
            tidy.parse(is, fos);
        } catch (FileNotFoundException e) {
            mLogger.error("Exception occured while converting HTML to XHTML. Exception : ",e);
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    fos = null;
                }
                fos = null;
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                }
                is = null;
            }
        }
        mLogger.debug("Exiting getXHTMLFromHTML()");
        return outputFile;
    }

}
