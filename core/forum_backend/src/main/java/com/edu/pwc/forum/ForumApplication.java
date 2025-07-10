package com.edu.pwc.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.edu.pwc.forum")
@EnableJpaRepositories(basePackages = "com.edu.pwc.forum.persistence.repositories")
@EntityScan(basePackages = "com.edu.pwc.forum.persistence.entity")

public class ForumApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}
}
