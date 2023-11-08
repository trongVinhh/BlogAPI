package com.ltv.blog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info (
				title = "Spring boot Blog app REST API ",
				description = "Documentations Rest APIs",
				version = "v1.0",
				contact = @Contact(
						name = "TrongVinh",
						email = "lamtrongvinh2003@gmai.com"
				)
		)
)
public class BlogApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
	}

}
