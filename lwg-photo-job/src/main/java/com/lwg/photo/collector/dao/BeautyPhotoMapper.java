package com.lwg.photo.collector.dao;

import java.util.List;

import com.lwg.photo.collector.model.domain.BeautyPhoto;

public interface BeautyPhotoMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(BeautyPhoto record);

    int insertSelective(BeautyPhoto record);
    
    int insertBatch(List<BeautyPhoto> records);

    BeautyPhoto selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(BeautyPhoto record);

    int updateByPrimaryKey(BeautyPhoto record);
}