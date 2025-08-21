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
/*
package org.mbc.czo.function.common.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    private String uploadPath; // @Value 어노테이션은 필드 선언 위에 있어야 함

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 파일 접근을 위한 리소스 핸들러
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath); // file: 프로토콜 추가 필요

        // 정적 리소스 접근을 위한 리소스 핸들러 (주석 해제 권장)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/templates/");
    }
}*/
