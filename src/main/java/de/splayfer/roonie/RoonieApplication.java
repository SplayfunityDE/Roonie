package de.splayfer.roonie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
@EnableScheduling
public class RoonieApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoonieApplication.class, args);
	}

}
