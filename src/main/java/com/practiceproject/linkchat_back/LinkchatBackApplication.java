package com.practiceproject.linkchat_back;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LinkchatBackApplication {

//	@Bean
//	CommandLineRunner init(UserRepository repo) {
//		return args -> {
//			// Initialize the database with a default user
//			if (repo.findByUsername("admin") == null) {
//				User admin = new User();
//				admin.setUsername("admin");
//				admin.setPassword("test");
//				repo.save(admin);
//			}
//		};
//	}

	public static void main(String[] args) {



		SpringApplication.run(LinkchatBackApplication.class, args);
	}
}
