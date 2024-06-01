package com.kn.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application {

	@Value("${allowed.origins}")
    	private String[] allowedOrigins;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public WebMvcConfigurer configure() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry reg) {
				reg.addMapping("/**").allowCredentials(true).allowedHeaders("*").maxAge(3600).allowedOrigins(allowedOrigins);
				//reg.addMapping("/**").allowedOrigins("http://localhost:3000").allowCredentials(true).allowedHeaders("*").maxAge(3600);
			}
		};
	}

}
