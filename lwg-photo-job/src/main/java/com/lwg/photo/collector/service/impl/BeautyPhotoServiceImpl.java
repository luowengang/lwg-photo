package com.lwg.photo.collector.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lwg.photo.collector.dao.BeautyPhotoMapper;
import com.lwg.photo.collector.model.domain.BeautyPhoto;
import com.lwg.photo.collector.service.IBeautyPhotoService;

@Service("beautyPhotoService")
public class BeautyPhotoServiceImpl implements IBeautyPhotoService {

	@Resource
	private BeautyPhotoMapper photoDao;

	@Override
	public BeautyPhoto getBeautyPhoto(String id) {

		return photoDao.selectByPrimaryKey(id);
	}

	@Override
	public int saveBeautyPhotos(List<BeautyPhoto> photos) {

		return photoDao.insertBatch(photos);
	}

}
