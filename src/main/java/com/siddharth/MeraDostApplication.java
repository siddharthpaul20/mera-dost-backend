package com.siddharth;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.siddharth.FileStorage.FileStorageService;

@SpringBootApplication
public class MeraDostApplication {
	
	static final Logger logger = Logger.getLogger(MeraDostApplication.class);

	public static void main(String[] args)
	{
		logger.debug("Entering main()");
		SpringApplication.run(MeraDostApplication.class, args);
		logger.debug("Exiting main()");
	}

}
