package com.lwg.photo.show.service;

import com.github.pagehelper.Page;
import com.lwg.photo.show.model.domain.BeautyPhoto;

public interface IBeautyPhotoService {
	
	BeautyPhoto getBeautyPhoto(String uuid);
	
	Page<BeautyPhoto> selectPhotoInPage(int pageNum, int pageSize);
	
	
}
