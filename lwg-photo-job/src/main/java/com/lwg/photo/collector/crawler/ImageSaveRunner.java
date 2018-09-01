package com.lwg.photo.collector.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.lwg.photo.collector.service.impl.PhotoSaveService;

@Component
public class ImageSaveRunner implements ApplicationRunner {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhotoSaveService photoSaveController;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		
		Runnable runner = new Runnable(){
			@Override
			public void run() {
				logger.info("Start saving photo on thread:" + Thread.currentThread().getName());
				photoSaveController.startSavingPhoto();
			}
			
		};
		Thread thread = new Thread(runner); 
		thread.start();
		
		
	}
}
