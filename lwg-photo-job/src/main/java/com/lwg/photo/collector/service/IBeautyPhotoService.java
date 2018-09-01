package com.lwg.photo.collector.service;

import java.util.List;

import com.lwg.photo.collector.model.domain.BeautyPhoto;

public interface IBeautyPhotoService {
	
	BeautyPhoto getBeautyPhoto(String id);
	
	int saveBeautyPhotos(List<BeautyPhoto> photos);
}
