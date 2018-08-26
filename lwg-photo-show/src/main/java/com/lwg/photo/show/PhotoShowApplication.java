package com.lwg.photo.show;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lwg.photo.show.dao")
public class PhotoShowApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoShowApplication.class, args);
	}
}
