package com.lwg.photo.collector.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lwg.photo.collector.model.domain.BeautyPhoto;
import com.lwg.photo.collector.service.IBeautyPhotoService;

/**
 * 实现单线程写库的操作
 * 
 * @author Administrator
 *
 */
@Component
public class PhotoSaveService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ConcurrentLinkedQueue<BeautyPhoto> photoQueue = new ConcurrentLinkedQueue<>();
	
	@Autowired
	private IBeautyPhotoService photoService;

	private volatile boolean quickrSave = false;

	private static final int MAX_BULK_SAVE_NUM = 50;

	public void putSavingPhoto(BeautyPhoto photo) {
		photoQueue.add(photo);
	}

	public void startSavingPhoto() {
		List<BeautyPhoto> photoList = new ArrayList<>();
		
		while(true) {
			photoList.clear();
			if (photoQueue.size() >= MAX_BULK_SAVE_NUM) {
				for (int i = 0; i < MAX_BULK_SAVE_NUM; i++) {
					BeautyPhoto item = photoQueue.poll();
					if (item != null) {
						photoList.add(item);
					}
				}
				bulkSavePhoto(photoList);

			} else if (quickrSave) {
				BeautyPhoto item = photoQueue.poll();
				if (item != null) {
					photoList.add(item);
					bulkSavePhoto(photoList);
				} else {
					try {
						Thread.sleep(500); // 等待500ms
					} catch (InterruptedException e) {
						logger.error("", e);
					}
				}

			}
		}
		
	}
	
	public boolean isRefreshSave() {
		return quickrSave;
	}

	public void setRefreshSave(boolean refreshSave) {
		this.quickrSave = refreshSave;
	}


	private void bulkSavePhoto(List<BeautyPhoto> photoList) {
		if (photoList.isEmpty()) {
			return;
		}
		try {
			photoService.saveBeautyPhotos(photoList);
		}catch (Exception e) {
			logger.error("", e);
		}
		
		
	}
}
