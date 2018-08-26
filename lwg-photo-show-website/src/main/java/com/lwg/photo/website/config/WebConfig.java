//package com.lwg.photo.website.config;
//
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/static/**")
//                .addResourceLocations("/")
//                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
//    }
//
//}
