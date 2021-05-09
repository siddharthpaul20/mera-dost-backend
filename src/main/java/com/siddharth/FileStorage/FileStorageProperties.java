package com.siddharth.FileStorage;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private static FileStorageProperties mFileStorageProperties;
    
    public FileStorageProperties()
    {
    	mFileStorageProperties = this;
    }
    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
    
    public static FileStorageProperties getInstance()
    {
    	return mFileStorageProperties;
    }
}
