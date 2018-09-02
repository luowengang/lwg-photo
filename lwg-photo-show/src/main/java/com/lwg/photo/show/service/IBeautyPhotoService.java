package com.lwg.photo.show.service;

import java.util.List;

import com.lwg.photo.show.model.domain.BeautyPhoto;

public interface IBeautyPhotoService {
	
	BeautyPhoto getBeautyPhoto(String uuid);
	
	List<BeautyPhoto> selectPhotoInPage(int startIndex, int endIndex);
	
	
}
