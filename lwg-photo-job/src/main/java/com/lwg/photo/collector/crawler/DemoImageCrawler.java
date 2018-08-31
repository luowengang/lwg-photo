package com.lwg.photo.collector.crawler;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;
import cn.edu.hfut.dmic.webcollector.util.MD5Utils;

/**
 * WebCollector抓取图片的例子
 * 
 * 
 */
public class DemoImageCrawler extends BreadthCrawler {
	private File baseDir = new File("images");
	
	private String imgUrlPrefix = "";

	private ConcurrentMap<String, ImageElement> imgFolderMap= new ConcurrentHashMap<>();

	public DemoImageCrawler(CrawlerConfig cfg) {
		super(cfg.getCrawlDbPath(), true);

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

		System.out.println(cfg.toString());

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
				
				String imgId = imgElem.getId();
				// 根据图片MD5生成文件名
				String fileName = String.format("%s.%s", imgId + "_" + MD5Utils.md5(image),
						extensionName);
				File imageFile = new File(parentFile, fileName);
				FileUtils.write(imageFile, image);
				System.out.println("保存图片 " + page.url() + " 到 " + imageFile.getAbsolutePath());
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
			String tilte = doc.title();
			int titleLength = tilte.length()>40? 40:tilte.length();
			String folderTitle = doc.title().substring(0, titleLength);
			Elements elems = doc.select("img[src^=" + this.imgUrlPrefix +"]");
			if(elems == null) {
				return;
			}
			for(Element elem : elems) {
				String imgUrl = elem.attr("src");
				String imgId = elem.attr("id");
				this.imgFolderMap.putIfAbsent(imgUrl, new ImageElement(imgUrl, imgId, folderTitle));
				System.out.println("---====" + imgUrl);
			}
		}
	}

	private File createParentFile(ImageElement imgElem) {	

		File parentFile = baseDir;
		if(imgElem != null) {
			parentFile = new File(baseDir, imgElem.getTitle());
			if (!parentFile.exists()) {
				boolean result = mkdirParentFile(parentFile);
				if (!result) {
					ExceptionUtils.fail(new FileSystemException(parentFile.getPath()));
				}
			}
		}
		
		return parentFile;
	}

	private synchronized boolean mkdirParentFile(File file) {
		if (!file.exists()) {
			return file.mkdirs();			
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		 DemoImageCrawler demoImageCrawler = new DemoImageCrawler("crawl_db");
		 demoImageCrawler.setRequester(new OkHttpRequester());
		 // 设置为断点爬取，否则每次开启爬虫都会重新爬取
		 // demoImageCrawler.setResumable(true);
		 demoImageCrawler.start(4);


	}
}