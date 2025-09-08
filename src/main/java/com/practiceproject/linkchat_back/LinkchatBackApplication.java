package com.practiceproject.linkchat_back;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class LinkchatBackApplication {

//	@Bean
//	CommandLineRunner init(UserRepository repo, BCryptPasswordEncoder passwordEncoder) {
//		return args -> {
//			if (repo.findByUsername("admin") == null) {
//				User admin = new User();
//				admin.setUsername("admin");
//				admin.setPassword(passwordEncoder.encode("123456"));
//				repo.save(admin);
//			}
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(LinkchatBackApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsyncEmail-");
		executor.initialize();
		return executor;
	}
}
