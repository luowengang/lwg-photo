package com.lwg.photo.show.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lwg.photo.show.dao.BeautyPhotoMapper;
import com.lwg.photo.show.model.domain.BeautyPhoto;
import com.lwg.photo.show.service.IBeautyPhotoService;


@Service("beautyPhotoService")
public class BeautyPhotoServiceImpl implements IBeautyPhotoService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BeautyPhotoMapper photoDao;
	
	@Override
	public BeautyPhoto getBeautyPhoto(String id) {

		return photoDao.selectByPrimaryKey(id);
	}

	@Override
	public List<BeautyPhoto> selectPhotoInPage(int startIndex, int endIndex) {
		PageHelper.startPage(startIndex, endIndex);
        List<BeautyPhoto> photos = photoDao.selectAll();
        logger.info("Total: " + ((Page) photos).getTotal());
        return photos; 
	}

}
