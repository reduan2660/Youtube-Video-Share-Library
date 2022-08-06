package com.ytvideoshare.backend;

import com.ytvideoshare.backend.domain.Role;
import com.ytvideoshare.backend.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class YoutubeVideoSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoutubeVideoSharingApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	/**
	 * Saves a role for user
	 * @param appUserService AppUserService
	 */
	@Bean
	CommandLineRunner run(AppUserService appUserService){
		return args -> {
//			Role adminRole = appUserService.saveRole(new Role(null, "ROLE_USER"));
		};
	}

}
