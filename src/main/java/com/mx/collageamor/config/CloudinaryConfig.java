package com.mx.collageamor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
@Configuration

public class CloudinaryConfig {
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				"Cloud_name", "dprsvhjkn",
				"api_key", "269743765494739",
				"api_secret", "Be7jPxwJ2Omeh7VId-J1loUXSy4",
				"secure", true
				
				
				
				
				));
	}
}
