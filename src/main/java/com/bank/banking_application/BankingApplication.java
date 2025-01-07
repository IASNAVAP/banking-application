package com.bank.banking_application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info=@Info(
                title="Pavan's Banking Application",
                description = "Backend Rest APIs",
                version = "v1.0",
                contact =  @Contact(
                      name = "pavan sai",
                      email = "vyamasanipavansai7@gmail.com",
                      url = "https://github.com/IASNAVAP"
                ),
                license = @License(
                        name = "Pavan's project",
                        url="https://github.com/IASNAVAP"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Pavan's Banking Application",
                url ="https://github.com/IASNAVAP"
        )
)
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}
