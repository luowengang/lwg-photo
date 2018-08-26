package com.lwg.photo.job.dao;

import com.lwg.photo.job.model.domain.BeautyPhoto;

public interface BeautyPhotoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BeautyPhoto record);

    int insertSelective(BeautyPhoto record);

    BeautyPhoto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BeautyPhoto record);

    int updateByPrimaryKey(BeautyPhoto record);
}