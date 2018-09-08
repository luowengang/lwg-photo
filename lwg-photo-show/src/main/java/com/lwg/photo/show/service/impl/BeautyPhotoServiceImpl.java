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
	public Page<BeautyPhoto> selectPhotoInPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
        List<BeautyPhoto> photos = photoDao.selectAll();
        Page<BeautyPhoto> photoPage = (Page<BeautyPhoto>) photos;
        logger.info("Total: " + photoPage.getTotal());
        return photoPage; 
	}

	@Override
	public List<BeautyPhoto> selectByTitle(String title) {
		return photoDao.selectByTitle(title);
	}

	@Override
	public List<String> selectAllTitles() {
		return photoDao.selectAllTitles();
	}

}
