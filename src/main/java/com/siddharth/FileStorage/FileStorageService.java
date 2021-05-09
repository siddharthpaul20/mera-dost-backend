package com.siddharth.FileStorage;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.siddharth.commons.Constants;
import com.siddharth.commons.PropertyReader;
import com.siddharth.commons.exceptions.FileNotFoundException;
import com.siddharth.commons.exceptions.FileStorageException;


public class FileStorageService {
	static final Logger logger = Logger.getLogger(FileStorageService.class);
    private Path fileStorageLocation;
    private Path convertedFileStorageLocation;
    private PropertyReader mpropReader = null;

    
    public FileStorageService()
    {
    	mpropReader = new PropertyReader();
    	String downloadDirectoryPath = mpropReader.getProperty(Constants.DOWNLOAD_DIRECTORY_PATH);
    	if(null != downloadDirectoryPath)
    	{
    		this.fileStorageLocation = Paths.get(mpropReader.getProperty(Constants.DOWNLOAD_DIRECTORY_PATH))
                    .toAbsolutePath().normalize();
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
            	logger.error("Could not create the directory where the uploaded files will be stored.", ex);
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
    	}
    	String convertedFileStorageLocation = mpropReader.getProperty(Constants.UPLOAD_DIRECTORY_PROPERTY);
    	if(null != convertedFileStorageLocation)
    	{
    		this.convertedFileStorageLocation = Paths.get(mpropReader.getProperty(Constants.UPLOAD_DIRECTORY_PROPERTY))
                    .toAbsolutePath().normalize();
            try {
                Files.createDirectories(this.convertedFileStorageLocation);
            } catch (Exception ex) {
            	logger.error("Could not create the directory where the uploaded files will be stored.", ex);
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
    	}
        
    }
    
    public Path getFileStorageLocation() {
		return fileStorageLocation;
	}

	public Path getConvertedFileStorageLocation() {
		return convertedFileStorageLocation;
	}

	public PropertyReader getMpropReader() {
		return mpropReader;
	}

	public boolean makeTheDirectoriesInRelativePath(String relativePath)
    {
    	logger.debug("Entering makeTheDirectoriesInRelativePath()");
    	logger.info(relativePath);
    	int positionOfLastSlash = relativePath.lastIndexOf('\\');
    	String excludingTheFile = relativePath.substring(0,positionOfLastSlash);
    	logger.info(excludingTheFile);
    	File f = new File(excludingTheFile);
    	logger.debug("Exiting makeTheDirectoriesInRelativePath()");
    	return f.mkdirs();
    }

    public String storeFile(MultipartFile file,String relativePath) {
    	logger.debug("Entering storeFile()");
    	logger.info("Inside storeFile ::: file :--->  "+file);
    	logger.info("Inside storeFile ::: relativePath :--->  "+relativePath);
    	// Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("fileName ::: -->  "+fileName);
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
            	logger.error("Sorry! Filename contains invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            //Changes made by me
             
            makeTheDirectoriesInRelativePath(this.fileStorageLocation.resolve(relativePath).toString());
            Path targetLocation;
            if(makeTheDirectoriesInRelativePath(this.fileStorageLocation.resolve(relativePath).toString()))
            {
            	logger.info("true is returned.");
            	targetLocation = this.fileStorageLocation.resolve(relativePath);
            }
            else
            {
            	logger.info("false is returned.");
            	targetLocation = this.fileStorageLocation.resolve(relativePath);
            }
            //Changes made by me
            
            // Copy file to the target location (Replacing existing file with the same name)
            //Path targetLocation = this.fileStorageLocation.resolve(relativePath);
            logger.info("targetLocation ::: -->  "+targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            logger.debug("Exiting storeFile()");
            return fileName;
        } catch (IOException ex) {
        	logger.error("Could not store file " + fileName + ". Please try again!", ex);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
    	logger.debug("Entering loadFileAsResource()");
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
            	logger.debug("Exiting loadFileAsResource()");
                return resource;
            } else {
            	logger.error("File not found " + fileName);
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
        	logger.error("File not found " + fileName);
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
        
    }
}
