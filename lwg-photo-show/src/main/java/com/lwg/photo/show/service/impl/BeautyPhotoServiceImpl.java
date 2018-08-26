package com.lwg.photo.show.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lwg.photo.show.dao.BeautyPhotoMapper;
import com.lwg.photo.show.model.domain.BeautyPhoto;
import com.lwg.photo.show.service.IBeautyPhotoService;

@Service("beautyPhotoService")
public class BeautyPhotoServiceImpl implements IBeautyPhotoService{
	
	@Resource
	private BeautyPhotoMapper photoDao;
	
	@Override
	public BeautyPhoto getBeautyPhoto(String id) {

		return photoDao.selectByPrimaryKey(id);
	}

}
