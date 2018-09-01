package com.lwg.photo.collector.crawler;

public class ImageElement {
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ImageElement(String url, String id, String title) {
		super();
		this.url = url;
		this.id = id;
		this.title = title;
	}
	private String url;
	private String id;
	private String title;
	
	
}
