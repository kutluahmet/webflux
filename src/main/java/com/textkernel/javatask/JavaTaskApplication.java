package com.textkernel.javatask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaTaskApplication {

/*	private final User user;

	public JavaTaskApplication(User user) {
		this.user = user;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(JavaTaskApplication.class, args);
	}

/*	@Bean
	public CommandLineRunner initDatabase(){
		return args -> {
			user;
		};
	}*/

}
