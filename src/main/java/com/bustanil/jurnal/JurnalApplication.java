package com.bustanil.jurnal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class JurnalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JurnalApplication.class, args);
	}
}
