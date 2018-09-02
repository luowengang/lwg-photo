package com.lwg.photo.show.dao;

import com.lwg.photo.show.model.domain.BeautyPhoto;
import com.lwg.photo.show.model.domain.BeautyPhotoExample;
import java.util.List;

public interface BeautyPhotoMapper {
    int countByExample(BeautyPhotoExample example);

    int deleteByPrimaryKey(String uuid);

    int insert(BeautyPhoto record);

    int insertSelective(BeautyPhoto record);

    List<BeautyPhoto> selectByExample(BeautyPhotoExample example);

    BeautyPhoto selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(BeautyPhoto record);

    int updateByPrimaryKey(BeautyPhoto record);
    
    List<BeautyPhoto> selectAll();
}