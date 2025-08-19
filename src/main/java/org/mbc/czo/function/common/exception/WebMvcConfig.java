package org.mbc.czo.function.common.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // 웹 브라우저에 입력하는 url에 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어오기
                .addResourceLocations(uploadPath);
     //   registry.addResourceHandler("/**")
     //          .addResourceLocations("classpath:/templates", "classpath:/static/");
    }

    @Value("${uploadPath}")
    String uploadPath;


}