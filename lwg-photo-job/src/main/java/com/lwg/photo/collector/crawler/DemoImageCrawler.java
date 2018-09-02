package com.lwg.photo.collector.crawler;

import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lwg.photo.collector.model.domain.BeautyPhoto;
import com.lwg.photo.collector.service.impl.PhotoSaveService;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;
import cn.edu.hfut.dmic.webcollector.util.MD5Utils;

/**
 * WebCollector抓取图片的例子
 * 
 * 
 */
@Component
public class DemoImageCrawler extends BreadthCrawler {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private File baseDir = new File("images");
	
	private String imgUrlPrefix = "";

	
	@Autowired
	private PhotoSaveService photoSaveService;

	private ConcurrentMap<String, ImageElement> imgFolderMap= new ConcurrentHashMap<>();
	
	@Autowired
	public DemoImageCrawler(CrawlerConfig cfg) {
		super(cfg.getCrawlDbPath(), true);
		
		this.baseDir = new File(cfg.getSaveImgPath());

		// 只有在autoParse和autoDetectImg都为true的情况下
		// 爬虫才会自动解析图片链接
		 getConf().setAutoDetectImg(true);

		// 如果使用默认的Requester，需要像下面这样设置一下网页大小上限
		// 否则可能会获得一个不完整的页面
		// 下面这行将页面大小上限设置为10M
		getConf().setMaxReceiveSize(1024 * 1024 * 10);

		// 添加种子URL
		String homePage = cfg.getHomePage();
		addSeed(homePage); // http://www.meishij.net http://www.yiren21.com/
		// 限定爬取范围
		String[] regexArray = cfg.getFilterRegex();
		if (regexArray != null) {
			for (String regex : regexArray) {
				addRegex(regex);
			}
		}
		
		this.imgUrlPrefix = cfg.getImgUrlPrefix();

		setThreads(cfg.getThreads());

		logger.info(cfg.toString());

	}

	/**
	 * 构造一个基于伯克利DB的爬虫 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
	 * 不同任务不要使用相同的crawlPath 两个使用相同crawlPath的爬虫并行爬取会产生错误
	 *
	 * @param crawlPath
	 *            伯克利DB使用的文件夹
	 */
	public DemoImageCrawler(String crawlPath) {
		super(crawlPath, true);

		// 只有在autoParse和autoDetectImg都为true的情况下
		// 爬虫才会自动解析图片链接
		getConf().setAutoDetectImg(true);

		// 如果使用默认的Requester，需要像下面这样设置一下网页大小上限
		// 否则可能会获得一个不完整的页面
		// 下面这行将页面大小上限设置为10M
		getConf().setMaxReceiveSize(1024 * 1024 * 10);

		// 添加种子URL
		String homePage = "http://www.yiren21.com/se/yazhousetu/621841.html";
		this.imgUrlPrefix = "http://cache01.pagoad.com";
		addSeed(homePage); // http://www.meishij.net
		// 限定爬取范围
		addRegex(homePage + ".*");
		addRegex("http://cache01.pagoad.com/.*");
		addRegex("-.*#.*");
		addRegex("-.*\\?.*");

		setThreads(2);

		// System.out.println(this.crawlerConfig.toString());

	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		// 根据http头中的Content-Type信息来判断当前资源是网页还是图片
		String contentType = page.contentType();
				
		// 根据Content-Type判断是否为图片
		if (contentType != null && contentType.startsWith("image")) {
			// 从Content-Type中获取图片扩展名
			String extensionName = contentType.split("/")[1];
			try {
				byte[] image = page.content();
				
				String pageUrl = page.url();
				ImageElement imgElem = this.imgFolderMap.get(pageUrl);
				File parentFile = createParentFile(imgElem);
				
				if(imgElem == null) {					
					return;
				}
				
				String imgAltId = imgElem.getId();
				String imgId =  MD5Utils.md5(image);
				
				// 根据图片MD5生成文件名
				String fileName = String.format("%s.%s", imgAltId + "_" + imgId,
						extensionName);
				File imageFile = new File(parentFile, fileName);
				FileUtils.write(imageFile, image);				
				logger.info("保存图片 " + page.url() + " 到 " + imageFile.getAbsolutePath());
				
				BeautyPhoto photo = new BeautyPhoto();
				photo.setCategory("Life");
				photo.setCollecttime(new Date());
				photo.setCollecturl(imgElem.getUrl());
				photo.setImgid(imgId);
				photo.setLocation(imageFile.getParentFile().getName() + File.separator + imageFile.getName());
				photo.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
				photo.setTitle(imgElem.getTitle());
				photoSaveService.putSavingPhoto(photo);
			} catch (Exception e) {
				ExceptionUtils.fail(e);
			}
		} else if(contentType != null && contentType.endsWith("html")){
			filterImgCollectionHtmlPage(page);
		}
	}
	
	
	private void filterImgCollectionHtmlPage(Page page) {
		String url = page.url();
		if(url.endsWith(".html")) {
			Document doc = page.doc();
			String title = doc.title();
			String[] titleSeg = title.split("-");
			if(titleSeg != null && titleSeg.length>0) {
				title = titleSeg[0];
			} else {
				int titleLength = title.length()>40? 40:title.length();
				title = doc.title().substring(0, titleLength);
			}
			
			title = filterFileChar(title.trim()); // 要去掉空格
			
			
			Elements elems = doc.select("img[src^=" + this.imgUrlPrefix +"]");
			if(elems == null) {
				return;
			}
			for(Element elem : elems) {
				String imgUrl = elem.attr("src");
				String imgId = elem.attr("id");
				this.imgFolderMap.putIfAbsent(imgUrl, new ImageElement(imgUrl, imgId, title));
				logger.info("imgUrl===" + imgUrl);
			}
		}
	}
	
	private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
	public static String filterFileChar(String str) {
		return str==null?"":FilePattern.matcher(str).replaceAll("");
	}

	private File createParentFile(ImageElement imgElem) {	

		File parentFile = baseDir;
		if(imgElem != null) {
			parentFile = new File(baseDir, imgElem.getTitle());					
		}
		
		return parentFile;
	}


	public static void main(String[] args) throws Exception {
//		 DemoImageCrawler demoImageCrawler = new DemoImageCrawler("crawl_db");
//		 demoImageCrawler.setRequester(new OkHttpRequester());
//		 // 设置为断点爬取，否则每次开启爬虫都会重新爬取
//		 // demoImageCrawler.setResumable(true);
//		 demoImageCrawler.start(4);
		
	}
}