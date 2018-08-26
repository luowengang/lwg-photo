package com.lwg.photo.job.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lwg.photo.job.dao.BeautyPhotoMapper;
import com.lwg.photo.job.model.domain.BeautyPhoto;
import com.lwg.photo.job.service.IBeautyPhotoService;

@Service("beautyPhotoService")
public class BeautyPhotoServiceImpl implements IBeautyPhotoService{
	
	@Resource
	private BeautyPhotoMapper photoDao;
	
	@Override
	public BeautyPhoto getBeautyPhoto(String id) {

		return photoDao.selectByPrimaryKey(id);
	}

}
