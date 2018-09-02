package com.lwg.photo.show.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lwg.photo.show.model.domain.BeautyPhoto;
import com.lwg.photo.show.service.IBeautyPhotoService;

@RestController
@RequestMapping(value = "/photo")
public class PhotoShowController {
	
	@Resource
	private IBeautyPhotoService beautyPhotoService;
	
	//这里使用@RequestMapping注解表示该方法对应的二级上下文路径
    @RequestMapping(value = "/getPhotoByCategory", method = RequestMethod.GET)
    String getPhotoByCategory(@RequestParam(value = "category") String category){
    	BeautyPhoto photo = beautyPhotoService.getBeautyPhoto("1234567890");   
    	return photo.getTitle();
    }
    
    @RequestMapping(value = "/getPhoto", method = RequestMethod.GET)
    List<BeautyPhoto>  getPhotoByCategory(@RequestParam(value = "startIndex") int startIndex, @RequestParam(value = "endIndex") int endIndex ){
    	List<BeautyPhoto> photos = beautyPhotoService.selectPhotoInPage(startIndex, endIndex);   
    	return photos;
    }

}
