package com.lwg.photo.collector;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lwg.photo.collector.dao")
public class PhotoCollectorApplication {


	public static void main(String[] args) {
		SpringApplication.run(PhotoCollectorApplication.class, args);
		
	}
}
