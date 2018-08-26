package com.lwg.photo.collector.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;


@Component
public class ImageCrawlStarter implements ApplicationRunner {

	@Autowired
	private CrawlerConfig crawlerConfig;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		DemoImageCrawler imageCrawler = new DemoImageCrawler(crawlerConfig);
		
		imageCrawler.setRequester(new OkHttpRequester());
		// 设置为断点爬取，否则每次开启爬虫都会重新爬取
		// demoImageCrawler.setResumable(true);
		imageCrawler.start(crawlerConfig.getDepth());
		
	}
}
