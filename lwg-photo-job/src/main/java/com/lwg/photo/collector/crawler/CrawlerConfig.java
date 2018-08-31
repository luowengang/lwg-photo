package com.lwg.photo.collector.crawler;

import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String[] getFilterRegex() {
		return filterRegex;
	}

	public void setFilterRegex(String[] filterRegex) {
		this.filterRegex = filterRegex;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getCrawlDbPath() {
		return crawlDbPath;
	}

	public void setCrawlDbPath(String crawlDbPath) {
		this.crawlDbPath = crawlDbPath;
	}

	public String getSaveImgPath() {
		return saveImgPath;
	}
	
	private String homePage;
	public String getImgUrlPrefix() {
		return imgUrlPrefix;
	}

	public void setImgUrlPrefix(String imgUrlPrefix) {
		this.imgUrlPrefix = imgUrlPrefix;
	}

	public void setSaveImgPath(String saveImgPath) {
		this.saveImgPath = saveImgPath;
	}


	@Override
	public String toString() {
		return "CrawlerConfig [homePage=" + homePage + ", filterRegex=" + Arrays.toString(filterRegex) + ", threads="
				+ threads + ", depth=" + depth + ", crawlDbPath=" + crawlDbPath + ", saveImgPath=" + saveImgPath
				+ ", imgUrlPrefix=" + imgUrlPrefix + "]";
	}


	private String[] filterRegex;
	private int threads;
	private int depth;
	private String crawlDbPath;
	private String saveImgPath;
	private String imgUrlPrefix;

}
