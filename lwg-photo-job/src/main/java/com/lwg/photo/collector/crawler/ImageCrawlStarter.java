package com.lwg.photo.collector.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;


@Component
public class ImageCrawlStarter implements ApplicationRunner {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CrawlerConfig crawlerConfig;
	
	@Autowired
	private DemoImageCrawler imageCrawler;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Runnable runner = new Runnable(){
			@Override
			public void run() {

				logger.info("Start image crawler on thread:" + Thread.currentThread().getName());
				imageCrawler.setRequester(new OkHttpRequester());
				// 设置为断点爬取，否则每次开启爬虫都会重新爬取
				imageCrawler.setResumable(true);
				try {
					imageCrawler.start(crawlerConfig.getDepth());
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			
		};
		Thread thread = new Thread(runner); 
		thread.start();
		
		
	}
}
