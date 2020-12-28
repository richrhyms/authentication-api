package com.richotaru.authenticationapi;

import com.richotaru.authenticationapi.configuration.ServiceLayerConfiguration;
import com.richotaru.authenticationapi.configuration.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
		ValidationAutoConfiguration.class
})
@Import({ServiceLayerConfiguration.class, WebConfiguration.class})
public class AuthenticationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}

}
