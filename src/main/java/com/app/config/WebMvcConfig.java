package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.app.interceptor.ThymeleafLayoutInterceptor;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer   {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	 registry
         .addResourceHandler("/webjars/**")
         .addResourceLocations("/webjars/").resourceChain(false);
    }
}