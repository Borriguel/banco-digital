package dev.borriguel.bancodigital;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Banco Digital Rest API",
				description = "Documentação da API Rest Spring Boot",
				contact = @Contact(
						name = "Rodolpho Henrique",
						email = "rodolpho.omedio@gmail.com",
						url = "https://github.com/Borriguel"
				)
		)
)
public class BancoDigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoDigitalApplication.class, args);
	}

}
