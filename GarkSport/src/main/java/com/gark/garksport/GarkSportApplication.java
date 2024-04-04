package com.gark.garksport;

import com.gark.garksport.dto.authentication.RegisterRequest;
import com.gark.garksport.modal.Admin;
import com.gark.garksport.modal.enums.Role;
import com.gark.garksport.repository.UserRepository;
import com.gark.garksport.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import static com.gark.garksport.modal.enums.Role.ADMIN;
import com.gark.garksport.repository.UserRepository;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class GarkSportApplication {


	public static void main(String[] args) {
		SpringApplication.run(GarkSportApplication.class, args);
	//	Role.ADMIN.printPermissions();

	}


//	@Bean
//	public CommandLineRunner commandLineRunner(AuthenticationService service) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("AdminTest")
//					.lastname("AdminTest")
//					.email("admintest@gmail.com")
//					.password("password")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin));
//		};
//	}
}